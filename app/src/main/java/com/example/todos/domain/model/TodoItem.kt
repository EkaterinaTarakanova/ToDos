package com.example.todos.domain.model

import androidx.compose.ui.graphics.Color
import org.joda.time.DateTime
import java.util.UUID

data class TodoItem(
    val uid: String = UUID.randomUUID().toString(),
    val text: String,
    val importance: Importance,
    val color: Color? = Color.White,
    val deadline: DateTime? = null,
    val isDone: Boolean = false,
    val createdAt: Long? = System.currentTimeMillis(),
    val changedAt: Long? = System.currentTimeMillis()
)

enum class Importance(val russianName: String, val color: Color) {
    UNIMPORTANT(russianName = "Неважная", color = Color.Gray),
    ORDINARY(russianName = "Обычная", color = Color(0xFF22C55E)),
    IMPORTANT(russianName = "Важная", color = Color.Red)
}