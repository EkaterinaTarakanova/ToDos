package com.example.todos.viewModels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.todos.data.FileStorage
import com.example.todos.data.TodoItem

class TaskListViewModel(private val fileStorage: FileStorage) : ViewModel() {
    private val _todoList = mutableStateListOf<TodoItem>()
    val todosList: List<TodoItem> get() = _todoList

    init {
        loadTodos()
    }

    fun loadTodos() {
        fileStorage.loadTodosFromFile()
        _todoList.clear()
        _todoList.addAll(fileStorage.todoItems)
    }

    fun deleteTodoItem(todoItem: TodoItem) {
        fileStorage.deleteTodo(todoItem.uid)
        _todoList.removeAll { it.uid == todoItem.uid }
    }

    fun toggleStateChange(todoItem: TodoItem) {
        val updatedTodo = todoItem.copy(isDone = !todoItem.isDone)
        fileStorage.updateTodo(updatedTodo)
        val index = _todoList.indexOfFirst { it.uid == todoItem.uid }
        if (index != -1) {
            _todoList[index] = updatedTodo
        }
    }
}