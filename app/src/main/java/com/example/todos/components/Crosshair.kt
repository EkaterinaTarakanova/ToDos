package com.example.todos.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun Crosshair(
    position: Offset
) {
    Box(
        modifier = Modifier
            .size(24.dp)
            .offset {
                IntOffset(
                    x = (position.x - 12.dp.toPx()).roundToInt(),
                    y = (position.y - 12.dp.toPx()).roundToInt()
                )
            }
    ) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Crosshair",
            tint = Color.Black,
            modifier = Modifier.size(24.dp)
        )
    }
}