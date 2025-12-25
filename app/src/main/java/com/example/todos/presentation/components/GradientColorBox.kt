package com.example.todos.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun GradientColorBox(
    brightness: Float = 1f,
    modifier: Modifier = Modifier, onCanvasSize: ((Float, Float) -> Unit)? = null
) {
    Canvas(modifier = modifier) {
        if (onCanvasSize != null) {
            onCanvasSize(size.width, size.height)
        }
        val height = size.height

        val horizontalGradient = Brush.horizontalGradient(
            colors = listOf(
                Color.hsv(0f, 1f, brightness),
                Color.hsv(60f, 1f, brightness),
                Color.hsv(120f, 1f, brightness),
                Color.hsv(180f, 1f, brightness),
                Color.hsv(240f, 1f, brightness),
                Color.hsv(300f, 1f, brightness),
                Color.hsv(360f, 1f, brightness)
            )
        )

        val verticalGradient = Brush.verticalGradient(
            colors = listOf(
                Color.Transparent,
                Color.Gray.copy(alpha = brightness)
            ),
            startY = 0f,
            endY = height
        )

        drawRect(brush = horizontalGradient, topLeft = Offset(0f, 0f), size = size)
        drawRect(brush = verticalGradient, topLeft = Offset(0f, 0f), size = size)
    }
}