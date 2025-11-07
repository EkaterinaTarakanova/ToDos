package com.example.todos.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.todos.FileStorage
import com.example.todos.Importance
import com.example.todos.TodoItem
import com.example.todos.components.convertMillisToDate
import org.joda.time.DateTime
import java.util.UUID

class EditTaskViewModel(
    private val fileStorage: FileStorage,
    val todoId: String,
    val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val isDone = mutableStateOf(false)
    val text = mutableStateOf("")
    val onDateSelected = mutableStateOf<Long?>(System.currentTimeMillis())
    val selectedImportance = mutableStateOf(Importance.ORDINARY)
    val selectedColor = mutableStateOf<Color?>(null)
    val customColor = mutableStateOf<Color?>(null)
    var currentTask: TodoItem? = null

    init {
        val selectedColorFromPicker = savedStateHandle.get<Int>("selectedColor")?.let {
            Color(it)
        }
        if (selectedColorFromPicker != null) {
            customColor.value = selectedColorFromPicker
        }
        if (todoId != "new") {
            currentTask = fileStorage.todoItems.find { it.uid == todoId }
            currentTask?.let { loadTask(it) }
        }
    }

    fun loadTask(todoItem: TodoItem) {
        text.value = todoItem.text
        isDone.value = todoItem.isDone
        onDateSelected.value = todoItem.deadline?.millis
        selectedImportance.value = todoItem.importance
        selectedColor.value = todoItem.color
        customColor.value = todoItem.customColor
    }

    fun saveTask(): TodoItem {
        return if (todoId == "new") {
            val newTask = TodoItem(
                text = text.value,
                isDone = isDone.value,
                importance = selectedImportance.value,
                deadline = DateTime(onDateSelected.value),
                color = selectedColor.value,
                customColor = customColor.value
            )
            fileStorage.addNewTodo(newTask)
            newTask
        } else {
            val updatedTask = currentTask!!.copy(
                text = text.value,
                isDone = isDone.value,
                importance = selectedImportance.value,
                deadline = DateTime(onDateSelected.value),
                color = selectedColor.value,
                customColor = customColor.value
            )
            fileStorage.updateTodo(updatedTask)
            updatedTask
        }
    }
}