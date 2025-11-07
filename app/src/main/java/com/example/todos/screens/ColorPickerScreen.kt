package com.example.todos.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.todos.components.ColorPicker

@Composable
fun ColorPickerScreen(
    onClick: (Color, Offset) -> Unit,
    initialColor: Color,
    initialPosition: Offset?,
) {
    val currentColor = remember { mutableStateOf(initialColor) }
    val currentPosition = remember { mutableStateOf(initialPosition ?: Offset.Zero) }
    val currentBrightness = remember { mutableFloatStateOf(1f) }
    val canvasSize = remember { mutableStateOf(Offset(300f, 300f)) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row {
            SelectedColor(color = currentColor.value)
            Slider(
                value = currentBrightness.value,
                onValueChange = { newBrightness ->
                    currentBrightness.value = newBrightness
                    val hue = (currentPosition.value.x / canvasSize.value.x) * 360
                    val saturation = 1 - (currentPosition.value.y / canvasSize.value.y)
                    val newColor = Color.hsv(hue = hue, value = newBrightness, saturation = saturation)
                    currentColor.value = newColor
                },
                valueRange = 0f..1f
            )
        }
        ColorPicker(
            onColorChanged = { newColor -> currentColor.value = newColor },
            currentPosition = currentPosition,
            brightness = currentBrightness.value,
            onCanvasSize = { width, height -> canvasSize.value = Offset(width, height) }
        )
        Button(onClick = { onClick(currentColor.value, currentPosition.value) }) {
            Text(text = "Done")
        }
    }
}

@Composable
fun SelectedColor(color: Color) {
    Box(
        modifier = Modifier
            .size(60.dp)
            .background(color = color, shape = RoundedCornerShape(12.dp))
    )
}
