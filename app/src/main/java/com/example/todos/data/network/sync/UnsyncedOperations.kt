package com.example.todos.data.network.sync

import com.example.todos.domain.model.TodoItem

data class UnsyncedOperations(
    val type: String,
    val item: TodoItem?,
    val itemId: String?
)
