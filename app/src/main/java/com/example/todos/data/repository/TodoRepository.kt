package com.example.todos.data.repository

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.todos.data.local.FileStorage
import com.example.todos.data.network.RemoteServer
import com.example.todos.domain.model.TodoItem
import com.example.todos.data.network.sync.OperationsQueue
import com.example.todos.data.network.sync.SyncWorker
import com.example.todos.data.network.sync.UnsyncedOperations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class TodoRepository(
    private val fileStorage: FileStorage,
    private val remoteServer: RemoteServer,
    private val context: Context
) {
    fun getTodos(): Flow<List<TodoItem>> = flow {
        emit(fileStorage.todoItems.value)
        withContext(Dispatchers.IO) {
            try {
                val serverTasks = remoteServer.loadTodosFromServer()
                if (serverTasks.isNotEmpty()) {
                    fileStorage.saveAll(serverTasks)
                }
            } catch (e: Exception) {
                println("Ошибка: ${e.message}")
            }
        }
        emitAll(fileStorage.todoItems)
    }

    suspend fun addTodo(todoItem: TodoItem) {
        fileStorage.addNewTodo(todoItem)
        withContext(Dispatchers.IO) {
            try {
                remoteServer.addNewTodo(todoItem)
            } catch (e: Exception) {
                println("Ошибка при добавлении: $e")
                OperationsQueue.add(
                    UnsyncedOperations(
                        type = "ADD",
                        item = todoItem,
                        itemId = null
                    )
                )
                syncWithServer()
            }

        }
    }

    suspend fun deleteTodo(uid: String) {
        val deleteTodo = fileStorage.todoItems.value.find { it.uid == uid }
        if (deleteTodo != null) {
            fileStorage.deleteTodo(deleteTodo.uid)

            withContext(Dispatchers.IO) {
                try {
                    remoteServer.deleteTodo(uid)
                } catch (e: Exception) {
                    println("Ошибка при удалении: $e")
                    OperationsQueue.add(
                        UnsyncedOperations(
                            type = "DELETE",
                            item = null,
                            itemId = uid
                        )
                    )
                    syncWithServer()
                }
            }
        }
    }

    suspend fun updateTodo(updatedTodo: TodoItem) {
        fileStorage.updateTodo(updatedTodo)

        withContext(Dispatchers.IO) {
            try {
                remoteServer.updateTodo(updatedTodo)
            } catch (e: Exception) {
                println("Ошибка при обновлении: $e")
                OperationsQueue.add(
                    UnsyncedOperations(
                        type = "UPDATE",
                        item = updatedTodo,
                        itemId = null
                    )
                )
                syncWithServer()
            }
        }
    }

    fun getTodoById(uid: String): Flow<TodoItem?> = flow {
        emit(fileStorage.todoItems.value.find { it.uid == uid })

        withContext(Dispatchers.IO) {
            try {
                val serverTodo = remoteServer.getTodo(uid)
                emit(serverTodo)
            } catch (e: Exception) {
                OperationsQueue.add(
                    UnsyncedOperations
                        (
                        type = "GET",
                        item = null,
                        itemId = uid
                    )
                )
                syncWithServer()
            }
        }
    }

    private fun syncWithServer() {
        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

        val syncRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(constraints)
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 30, TimeUnit.SECONDS).build()

        WorkManager.getInstance(context)
            .enqueueUniqueWork("sync_todo", ExistingWorkPolicy.REPLACE, syncRequest)
    }
}
