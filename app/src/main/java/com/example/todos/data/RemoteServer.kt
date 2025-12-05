package com.example.todos.data

import kotlinx.coroutines.delay
import org.slf4j.LoggerFactory

class RemoteServer() {
    private val log = LoggerFactory.getLogger(RemoteServer::class.java)
    suspend fun addNewTodo(todoItem: TodoItem) {
        delay(2000)
        log.info("Добавляем новую задачу: ${todoItem.text} на сервер")
    }

    suspend fun deleteTodo(uid: String) {
        delay(2000)
        log.info("Удаляем задачу: ${uid} с сервера")
        saveTodosToServer()
    }

    suspend fun saveTodosToServer() {
        delay(2000)
        log.info("Сохраняем задачи на сервер")
    }

    suspend fun loadTodosFromServer(): List<TodoItem> {
        delay(2000)
        log.info("Загружаем задачи с сервера")
        return emptyList()
    }

    suspend fun updateTodo(updatedTodo: TodoItem) {
        delay(2000)
        log.info("Обновляем задачу: ${updatedTodo.text}")
    }

    suspend fun getTodoById(uid: String) : TodoItem?{
        delay(2000)
        log.info("Получаем задачу с сервера: ${uid}")
        return null
    }
}