package com.example.todos.components

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

@Composable
fun ColorPicker(
    onColorChanged: (Color) -> Unit,
    currentPosition: MutableState<Offset>,
    brightness: Float,
    onCanvasSize: (Float, Float) -> Unit
) {
    Box(
        modifier = Modifier
            .size(300.dp)
            .pointerInput(brightness) {
                detectDragGestures(
                    onDragStart = { offset -> currentPosition.value = offset },
                    onDrag = { change, dragAmount ->
                        currentPosition.value = Offset(
                            x = change.position.x.coerceIn(0f, size.width.toFloat()),
                            y = change.position.y.coerceIn(0f, size.height.toFloat())
                        )
                        val hue = (currentPosition.value.x / size.width) * 360
                        val saturation = 1 - (currentPosition.value.y / size.height)
                        val newColor = Color.hsv(
                            hue = hue,
                            value = brightness,
                            saturation = saturation
                        )
                        onColorChanged(newColor)
                    }
                )
            }
    ) {
        GradientColorBox(
            brightness = brightness,
            modifier = Modifier.fillMaxSize(),
            onCanvasSize = { width, height ->
                onCanvasSize(width, height)
            }
        )

        Crosshair(
            position = currentPosition.value
        )
    }
}
