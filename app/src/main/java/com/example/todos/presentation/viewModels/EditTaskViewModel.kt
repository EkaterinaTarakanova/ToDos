package com.example.todos.presentation.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todos.domain.model.Importance
import com.example.todos.domain.model.TodoItem
import com.example.todos.data.repository.TodoRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.joda.time.DateTime

class EditTaskViewModel(
    private val todoRepository: TodoRepository,
    val todoId: String,
    val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val isDone = mutableStateOf(false)
    val text = mutableStateOf("")
    val onDateSelected = mutableStateOf<Long?>(System.currentTimeMillis())
    val selectedImportance = mutableStateOf(Importance.ORDINARY)
    val selectedColor = mutableStateOf<Color?>(null)
    val customColor = mutableStateOf<Color?>(null)
    private var currentTask: TodoItem? = null

    init {
        savedStateHandle.get<Int>("selectedColor")?.let { colorInt ->
            customColor.value = Color(colorInt)
            selectedColor.value = customColor.value
        }
    }

    fun loadData() {
        viewModelScope.launch {
            val colorInt = savedStateHandle
                .getStateFlow<Int?>("selectedColor", null)
                .firstOrNull()

            colorInt?.let {
                customColor.value = Color(it)
                selectedColor.value = Color(it)
            }
        }

        if (todoId != "new") {
            viewModelScope.launch {
                val task = todoRepository.getTodoById(todoId).firstOrNull()
                currentTask = task
                task?.let { loadTask(it) }
            }
        }
    }

    fun loadTask(todoItem: TodoItem) {
        text.value = todoItem.text
        isDone.value = todoItem.isDone
        onDateSelected.value = todoItem.deadline?.millis
        selectedImportance.value = todoItem.importance
        selectedColor.value = todoItem.color
    }

    fun saveTask(onSaved: (TodoItem) -> Unit) {
        viewModelScope.launch {
            val taskToSave = if (todoId == "new") {
                TodoItem(
                    text = text.value,
                    isDone = isDone.value,
                    importance = selectedImportance.value,
                    deadline = DateTime(onDateSelected.value),
                    color = selectedColor.value,
                )
            } else {
                currentTask?.copy(
                    text = text.value,
                    isDone = isDone.value,
                    importance = selectedImportance.value,
                    deadline = DateTime(onDateSelected.value),
                    color = selectedColor.value,
                )
            }

            taskToSave?.let {

                if (todoId == "new") {
                    todoRepository.addTodo(it)
                } else {
                    todoRepository.updateTodo(it)
                }
                onSaved(it)
            }
        }
    }
}