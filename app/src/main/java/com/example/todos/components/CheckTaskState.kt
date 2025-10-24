package com.example.todos.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CheckTaskState(isChecked: MutableState<Boolean>) {
    Row(
        modifier = Modifier
            .height(84.dp)
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(16.dp))
            .background(color = Color(0xFFF3E8FF))
            .border(width = 2.dp, shape = RoundedCornerShape(16.dp), color = Color(0xFFC4B5FD)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isChecked.value,
            onCheckedChange = { isChecked.value = it },
            colors = CheckboxDefaults.colors(checkedColor = Color(0xFF10B981))
        )
        Text(text = "Дело сделано")
    }
}
