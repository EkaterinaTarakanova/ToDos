package com.example.todos.data.network

import android.content.Context
import com.example.todos.data.mapper.TodoMapper.toDto
import com.example.todos.domain.model.TodoItem
import com.example.todos.data.mapper.TodoMapper.toLocal
import com.example.todos.data.network.dto.TodoElementRequest
import com.example.todos.data.network.dto.TodoListRequest
import com.example.todos.data.storage.DeviceIdStorage
import com.example.todos.data.storage.RevisionStorage
import org.slf4j.LoggerFactory

class RemoteServer(private val context: Context) {
    private val api: ApiService by lazy {
        RetrofitClient(context).api
    }
    private val log = LoggerFactory.getLogger(RemoteServer::class.java)
    suspend fun addNewTodo(todoItem: TodoItem) {
        val response = api.addTodo(
            RevisionStorage.getRevision(),
            element = TodoElementRequest(todoItem.toDto(DeviceIdStorage.deviceId))
        )

        if (response.isSuccessful) {
            val networkResponse = response.body()
            log.info("Добавляем новую задачу: ${networkResponse} на сервер")
            networkResponse?.revision?.let { RevisionStorage.saveRevision(it) }
        } else {
            val errorBody = response.errorBody()?.string()
            log.error("Ошибка при добавлении. Код: ${response.code()}, Тело: $errorBody")

            throw retrofit2.HttpException(response)
        }
    }

    suspend fun deleteTodo(uid: String) {
        val response = api.deleteTodo(todoId = uid, revision = RevisionStorage.getRevision())
        if (response.isSuccessful) {
            val networkResponse = response.body()
            networkResponse?.revision?.let { RevisionStorage.saveRevision(it) }
        } else {
            val errorBody = response.errorBody()?.string()
            log.error("Ошибка при удалении: $errorBody")
            throw retrofit2.HttpException(response)
        }
    }

    suspend fun loadTodosFromServer(): List<TodoItem> {
        val response = api.getTodos()

        if (response.isSuccessful) {
            val networkResponse = response.body()
            log.info("Загружаем задачи с сервера $networkResponse")
            val localList = networkResponse?.list?.map { it.toLocal() } ?: emptyList()
            networkResponse?.revision?.let { RevisionStorage.saveRevision(it) }
            return localList
        } else {
            throw retrofit2.HttpException(response)
        }
    }

    suspend fun getTodo(todoItemId: String): TodoItem {
        val response = api.getTodo(todoId = todoItemId)

        if (response.isSuccessful) {
            val networkResponse = response.body()
            log.info("ГРузим задачку $networkResponse")
            val todo = networkResponse?.element?.toLocal()
            networkResponse?.revision?.let { RevisionStorage.saveRevision(it) }
            return todo ?: throw IllegalStateException("Не найден туду")
        } else {
            throw retrofit2.HttpException(response)
        }
    }

    suspend fun syncTodos(localList: List<TodoItem>): List<TodoItem> {
        val dtoList = localList.map { it.toDto(deviceId = DeviceIdStorage.deviceId) }
        val todoListRequest = TodoListRequest(dtoList)
        val response = api.updateTodos(
            RevisionStorage.getRevision(),
            todoListRequest
        )

        if (response.isSuccessful) {
            val networkResponse = response.body()
            log.info("Работает: $networkResponse")
            val newRevision = networkResponse?.revision
            if (newRevision != null) {
                RevisionStorage.saveRevision(newRevision)
            }
            val serverList = networkResponse?.list ?: emptyList()
            return serverList.map { it.toLocal() }
        } else {
            val errorBody = response.errorBody()?.string()
            log.error("Тело ошибки: $errorBody")

            throw retrofit2.HttpException(response)
        }
    }

    suspend fun updateTodo(updatedTodo: TodoItem) {
        val response = api.updateTodo(
            todoId = updatedTodo.uid,
            revision = RevisionStorage.getRevision(),
            element = TodoElementRequest(updatedTodo.toDto(DeviceIdStorage.deviceId))
        )
        if (response.isSuccessful) {
            val networkResponse = response.body()
            networkResponse?.revision?.let { RevisionStorage.saveRevision(it) }
            log.info("Обновляем задачу: ${networkResponse}")
        } else {
            val errorBody = response.errorBody()?.string()
            log.error("Ошибка 400 при обновлении: $errorBody")
            throw retrofit2.HttpException(response)
        }
    }
}