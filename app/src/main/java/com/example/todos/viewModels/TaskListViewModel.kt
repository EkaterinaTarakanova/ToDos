package com.example.todos.viewModels

import androidx.lifecycle.ViewModel
import com.example.todos.FileStorage
import com.example.todos.TodoItem

class TaskListViewModel(private val fileStorage: FileStorage) : ViewModel() {
    val todosList: List<TodoItem> get() = fileStorage.todoItems

    init {
        fileStorage.loadTodosFromFile()
    }

    fun deleteTodoItem(todoItem: TodoItem) {
        fileStorage.deleteTodo(todoItem.uid)
    }

    fun toggleStateChange(todoItem: TodoItem) {
        val updatedTodo = todoItem.copy(isDone = !todoItem.isDone)
        fileStorage.updateTodo(updatedTodo)

    }
}