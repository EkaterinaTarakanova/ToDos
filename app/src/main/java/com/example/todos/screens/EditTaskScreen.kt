package com.example.todos.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todos.viewModels.EditTaskViewModelFactory
import com.example.todos.data.TodoItem
import com.example.todos.components.CheckTaskState
import com.example.todos.components.DescriptionTextField
import com.example.todos.components.ImportancePicker
import com.example.todos.components.SelectedTimeInfo
import com.example.todos.components.TaskColorPicker
import com.example.todos.viewModels.EditTaskViewModel

@Composable
fun EditTaskScreen(
    onColorPickerClick: () -> Unit,
    onSaveTask: (TodoItem) -> Unit, editTaskViewModelFactory: EditTaskViewModelFactory
) {
    val viewModel: EditTaskViewModel = viewModel(factory = editTaskViewModelFactory)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .systemBarsPadding()
            .padding(horizontal = 22.dp)
            .padding(vertical = 32.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                DescriptionTextField(viewModel.isDone.value, viewModel.text)
                SelectedTimeInfo(viewModel.isDone.value, viewModel.onDateSelected)
                CheckTaskState(isChecked = viewModel.isDone)
                ImportancePicker(viewModel.isDone.value, viewModel.selectedImportance)
                TaskColorPicker(
                    customColor = viewModel.customColor.value,
                    onColorPickerClick = onColorPickerClick,
                    isDone = viewModel.isDone.value,
                    selectedColor = viewModel.selectedColor
                )
                TextButton(onClick = {
                    val savedTask = viewModel.saveTask()
                    onSaveTask(savedTask)
                }) {
                    Text(text = "Сохранить")
                }
            }
        }
    }
}












