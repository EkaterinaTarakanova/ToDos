package com.example.todos.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.todos.components.CheckTaskState
import com.example.todos.components.DescriptionTextField
import com.example.todos.components.ImportancePicker
import com.example.todos.components.SelectedTimeInfo
import com.example.todos.components.TaskColorPicker

@Composable
fun EditTaskScreen(onColorPickerClick: () -> Unit, customColor: Color?) {
    val state = rememberScrollState()
    val isDone = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .systemBarsPadding()
            .verticalScroll(state = state)
            .padding(horizontal = 22.dp)
            .padding(vertical = 32.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        DescriptionTextField(isDone.value)
        SelectedTimeInfo(isDone.value)
        CheckTaskState(isChecked = isDone)
        ImportancePicker(isDone.value)
        TaskColorPicker(
            customColor = customColor,
            onColorPickerClick = onColorPickerClick,
            isDone = isDone.value
        )
    }
}












