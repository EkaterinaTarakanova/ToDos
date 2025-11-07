package com.example.todos

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todos.viewModels.EditTaskViewModel
import com.example.todos.viewModels.TaskListViewModel

class TaskListViewModelFactory(private val fileStorage: FileStorage) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TaskListViewModel(fileStorage) as T
    }
}

class EditTaskViewModelFactory(
    private val fileStorage: FileStorage,
    private val todoId: String,
    private val savedStateHandle: SavedStateHandle
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditTaskViewModel(fileStorage, todoId, savedStateHandle) as T
    }
}