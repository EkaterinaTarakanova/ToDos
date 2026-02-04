package com.example.todos.data.network.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.todos.data.local.database.AppDatabase
import com.example.todos.data.local.database.datastorage.RoomDataStorage
import com.example.todos.domain.model.TodoItem
import com.example.todos.data.network.RemoteServer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class SyncWorker(
    appContext: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(appContext, workerParameters) {
    private val database = AppDatabase.getInstance(applicationContext)
    private val roomDataStorage = RoomDataStorage(database)
    private val remoteServer = RemoteServer(applicationContext)
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        while (OperationsQueue.get().isNotEmpty()) {
            val op = OperationsQueue.peek()
            try {
                if (op != null) {
                    val success = executeOperation(op = op)
                    if (success) {
                        OperationsQueue.removeFirst()
                    } else {
                        return@withContext handle400Error()
                    }
                }

            } catch (e: Exception) {
                when {
                    is500Error(e) -> return@withContext Result.retry()
                    is400Error(e) -> return@withContext handle400Error()
                    else -> return@withContext Result.failure()
                }
            }
        }

        return@withContext Result.success()
    }

    private suspend fun executeOperation(op: UnsyncedOperations): Boolean {
        return try {
            when (op.type) {
                "ADD" -> op.item?.let { remoteServer.addNewTodo(it) } != null
                "UPDATE" -> op.item?.let { remoteServer.updateTodo(it) } != null
                "DELETE" -> op.itemId?.let { remoteServer.deleteTodo(it) } != null
                "GET" -> op.itemId?.let { remoteServer.getTodo(it) } != null
            }
            true
        } catch (e: HttpException) {
            if (is500Error(e)) {
                throw e
            }
            false
        }
    }

    private suspend fun handle400Error(): Result {
        return try {
            val serverList = remoteServer.loadTodosFromServer()
            val localList = roomDataStorage.todoItems.first()
            val mergedList = mergeLists(localList, serverList)
            val finalList = remoteServer.syncTodos(mergedList)
            roomDataStorage.saveAll(finalList)
            OperationsQueue.clear()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    private fun is500Error(e: Exception): Boolean {
        return e is HttpException && e.code() == 500
    }

    private fun is400Error(e: Exception): Boolean {
        return e is HttpException && e.code() == 400
    }

    private fun mergeLists(local: List<TodoItem>, server: List<TodoItem>): List<TodoItem> {
        val result = mutableListOf<TodoItem>()
        val allIds = local.map { it.uid }.toSet() + server.map { it.uid }.toSet()

        for (id in allIds) {
            val localItem = local.find { it.uid == id }
            val serverItem = server.find { it.uid == id }

            when {
                localItem == null -> serverItem?.let { result.add(it) }
                serverItem == null -> result.add(localItem)
                else -> {
                    val newerItem = if ((localItem.changedAt ?: 0) > (serverItem.changedAt ?: 0)) {
                        localItem
                    } else {
                        serverItem
                    }
                    result.add(newerItem)
                }
            }
        }
        return result
    }
}