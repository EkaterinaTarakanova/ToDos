package com.example.todos.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ImportanceButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    isEnabled: Boolean = true
) {
    TextButton(
        onClick = onClick,
        enabled = isEnabled,
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .background(
                color = if (isSelected) Color(0xFFA78BFA) else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Text(
            text = text,
            color = when {
                !isEnabled -> Color.Gray
                isSelected -> Color.White
                else -> Color.Gray
            }
        )
    }
}


