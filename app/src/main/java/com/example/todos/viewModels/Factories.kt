package com.example.todos.viewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todos.data.FileStorage

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