package com.example.todos.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todos.data.TodoItem
import com.example.todos.repository.TodoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskListViewModel(private val todoRepository: TodoRepository) : ViewModel() {
    val todosList: StateFlow<List<TodoItem>> = todoRepository.getTodos()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun deleteTodoItem(todoItem: TodoItem) {
        viewModelScope.launch {
            todoRepository.deleteTodo(todoItem.uid)
        }
    }

    fun toggleStateChange(todoItem: TodoItem) {
        viewModelScope.launch {
            todoRepository.updateTodo(todoItem.copy(isDone = !todoItem.isDone))
        }
    }
}