package com.example.todos.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.todos.R


@Composable
fun ImportancePicker(isDone: Boolean) {
    val importanceLevels = listOf("Низкая", "Обычная", "Высокая")
    val selectedImportance = remember { mutableStateOf("Обычная") }

    Column(
        modifier = Modifier.alpha(if (isDone) 0.5f else 1f)
    ) {
        TextWithIcon(title = "Уровень важности", icon = R.drawable.importance_icon)
        Row(
            modifier = Modifier
                .height(84.dp)
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(16.dp))
                .background(if (isDone) Color.LightGray else Color(0xFFF3E8FF))
                .border(width = 2.dp, shape = RoundedCornerShape(16.dp), color = Color(0xFFC4B5FD)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
        ) {
            importanceLevels.forEach { level ->
                ImportanceButton(
                    text = level,
                    isSelected = selectedImportance.value == level,
                    onClick = { if (!isDone) selectedImportance.value = level },
                    isEnabled = !isDone
                )
            }
        }
    }
}