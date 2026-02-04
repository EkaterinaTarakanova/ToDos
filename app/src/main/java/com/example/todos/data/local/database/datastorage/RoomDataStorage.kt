package com.example.todos.data.local.database.datastorage

import com.example.todos.data.local.database.AppDatabase
import com.example.todos.data.local.database.mappers.toDomain
import com.example.todos.data.local.database.mappers.toEntity
import com.example.todos.domain.model.TodoItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomDataStorage(
    private val database: AppDatabase
) {
    private val dao = database.todoDao()

    val todoItems: Flow<List<TodoItem>>
        get() = dao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }

    suspend fun saveAll(todos: List<TodoItem>) {
        dao.saveAll(todos.map { it.toEntity() })
    }

    suspend fun addNewTodo(todo: TodoItem) {
        dao.save(todo.toEntity())
    }

    suspend fun deleteTodo(uid: String) {
        dao.deleteById(uid)
    }

    suspend fun updateTodo(todo: TodoItem) {
        dao.update(todo.toEntity())
    }

    suspend fun findById(uid: String): TodoItem? {
        return dao.getById(uid)?.toDomain()
    }
}