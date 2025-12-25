package com.example.todos.presentation.viewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todos.data.repository.TodoRepository

class TaskListViewModelFactory(private val todoRepository: TodoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TaskListViewModel(todoRepository) as T
    }
}

class EditTaskViewModelFactory(
    private val todoRepository: TodoRepository,
    private val todoId: String,
    private val savedStateHandle: SavedStateHandle
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditTaskViewModel(todoRepository, todoId, savedStateHandle) as T
    }
}