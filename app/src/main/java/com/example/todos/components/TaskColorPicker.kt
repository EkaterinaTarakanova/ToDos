package com.example.todos.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
fun TaskColorPicker(
    onColorPickerClick: () -> Unit,
    customColor: Color?, isDone: Boolean
) {
    val colors = listOf(
        Color(0xFF22C55E),
        Color(0xFFEF4444),
        Color(0xFFF97316),
        Color(0xFF8B5CF6),
        Color(0xFF3B82F6)
    )
    val selectedColor = remember { mutableStateOf<Color?>(null) }

    Column(modifier = Modifier.alpha(if(isDone) 0.5f else 1f)) {
        TextWithIcon(title = "Выбор цвета", icon = R.drawable.icon_calendar)
        Row(
            modifier = Modifier
                .height(84.dp)
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(16.dp))
                .background(if (isDone) Color.LightGray else Color(0xFFF3E8FF))
                .border(width = 2.dp, shape = RoundedCornerShape(16.dp), color = Color(0xFFC4B5FD)),
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            colors.forEach { color ->
                ColorItem(
                    color = color,
                    onClick = { if (!isDone) selectedColor.value = color },
                    isSelected = (selectedColor.value == color),
                    isEnabled = !isDone
                )
            }

            CustomColorPicker(
                isSelected = (customColor != null && selectedColor.value == null),
                onClick = { if (!isDone && customColor != null) selectedColor.value = null },
                onLongClick = { if (!isDone) onColorPickerClick() },
                customColor = customColor,
                isEnabled = !isDone
            )
        }
    }
}