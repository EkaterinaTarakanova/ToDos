package com.example.todos.repository

import com.example.todos.data.FileStorage
import com.example.todos.data.RemoteServer
import com.example.todos.data.TodoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class TodoRepository(private val fileStorage: FileStorage, private val remoteServer: RemoteServer) {
    fun getTodos(): Flow<List<TodoItem>> = flow {
        fileStorage.loadTodosFromFile()

        emit(fileStorage.todoItems.value)

        try {
            val serverTasks = withContext(Dispatchers.IO) {
                remoteServer.loadTodosFromServer()
            }

            if (serverTasks.isNotEmpty()) {
                val currentLocalTasks = fileStorage.todoItems.value
                var isChanged = false

                serverTasks.forEach { serverTask ->
                    val existingTask = currentLocalTasks.find { it.uid == serverTask.uid }
                    if (existingTask != null) {
                        if (existingTask != serverTask) {
                            fileStorage.updateTodo(serverTask)
                            isChanged = true
                        }
                    } else {
                        fileStorage.addNewTodo(serverTask)
                        isChanged = true
                    }
                }
                if (isChanged) {
                    emit(fileStorage.todoItems.value)
                }
            }
        } catch (e: Exception) {
            println("Ошибка сервера: ${e.message}")
        }
        emitAll(fileStorage.todoItems)
    }

    suspend fun addTodo(todoItem: TodoItem) {
        fileStorage.addNewTodo(todoItem)
        try {
            withContext(Dispatchers.IO) {
                remoteServer.addNewTodo(todoItem)
            }
        } catch (e: Exception) {
            println("Не удалось задачу отправить на сервер: ${e.message}")
        }
    }

    suspend fun deleteTodo(uid: String) {
        fileStorage.deleteTodo(uid)

        try {
            withContext(Dispatchers.IO) {
                remoteServer.deleteTodo(uid)
            }
        } catch (e: Exception) {
            println("Не удалось удалить задачу с сервера: ${e.message}")
        }
    }

    suspend fun updateTodo(updatedTodo: TodoItem) {
        fileStorage.updateTodo(updatedTodo)
        try {
            withContext(Dispatchers.IO) {
                remoteServer.updateTodo(updatedTodo)
            }
        } catch (e: Exception) {
            println("Ошибка: ${e.message}")
        }
    }

    fun getTodoById(uid: String): Flow<TodoItem?> = flow {
        emit(fileStorage.todoItems.value.find { it.uid == uid })
    }
}
