package com.example.todos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todos.viewModels.EditTaskViewModel
import com.example.todos.viewModels.TaskListViewModel

class TaskListViewModelFactory(private val fileStorage: FileStorage) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TaskListViewModel(fileStorage) as T
    }
}

class EditTaskViewModelFactory(private val fileStorage: FileStorage, private val todoId: String) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditTaskViewModel(fileStorage, todoId) as T
    }
}