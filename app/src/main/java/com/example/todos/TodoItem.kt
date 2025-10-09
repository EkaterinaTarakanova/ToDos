package com.example.todos

import androidx.compose.ui.graphics.Color
import org.joda.time.DateTime
import java.util.UUID

data class TodoItem(
    val uid: String = UUID.randomUUID().toString(),
    val text: String,
    val importance: Importance,
    val color: Color = Color.White,
    val deadline: DateTime? = null,
    val isDone: Boolean = false
)

enum class Importance {
    UNIMPORTANT, ORDINARY, IMPORTANT
}