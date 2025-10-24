package com.example.todos.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

@Composable
fun CustomColorPicker(
    isSelected: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    customColor: Color?,
    isEnabled: Boolean
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(shape = RoundedCornerShape(8.dp))
            .pointerInput(isEnabled) {
                detectTapGestures(
                    onTap = { onClick() },
                    onLongPress = { onLongClick() })
            },
        contentAlignment = Alignment.Center
    ) {
        if (customColor != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = if (isEnabled) customColor else customColor.copy(alpha = 0.5f))
            )
        } else {
            GradientColorBox(
                modifier = Modifier.fillMaxSize(),
                brightness = if (isEnabled) 1f else 0.5f
            )
        }

        if (isSelected && customColor != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(
                        width = 2.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(8.dp)
                    )
            )
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Selected",
                tint = Color.Black,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}