package com.example.todos

import androidx.compose.ui.graphics.Color
import org.joda.time.DateTime
import java.util.UUID

data class TodoItem(
    val uid: String = UUID.randomUUID().toString(),
    val text: String,
    val importance: Importance,
    val color: Color? = Color.White,
    val customColor: Color? = null,
    val deadline: DateTime? = null,
    val isDone: Boolean = false
)

enum class Importance(val russianName: String, val color: Color) {
    UNIMPORTANT(russianName = "Неважная", color = Color.Gray),
    ORDINARY(russianName = "Обычная", color = Color(0xFF22C55E)),
    IMPORTANT(russianName = "Важная", color = Color.Red)
}