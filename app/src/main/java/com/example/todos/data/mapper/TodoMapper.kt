package com.example.todos.data.mapper

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.todos.domain.model.Importance
import com.example.todos.domain.model.TodoItem
import com.example.todos.data.network.dto.TodoItemDTO
import org.joda.time.DateTime

object TodoMapper {
    fun TodoItemDTO.toLocal(): TodoItem {
        return TodoItem(
            uid = this.id,
            text = this.text,
            importance = when (this.importance) {
                "low" -> Importance.UNIMPORTANT
                "basic" -> Importance.ORDINARY
                "important" -> Importance.IMPORTANT
                else -> Importance.ORDINARY
            },
            isDone = this.done,
            color = getColorFromString(this.color),
            deadline = this.deadline?.let { DateTime(it) },
            createdAt = this.createdAt,
            changedAt = this.changedAt
        )
    }

    fun TodoItem.toDto(deviceId: String): TodoItemDTO {
        return TodoItemDTO(
            id = this.uid,
            text = this.text,
            importance = when (this.importance) {
                Importance.UNIMPORTANT -> "low"
                Importance.ORDINARY -> "basic"
                Importance.IMPORTANT -> "important"
            },
            deadline = this.deadline?.millis,
            done = this.isDone,
            color = this.color?.let { colorToHexString(it) },
            lastUpdatedBy = deviceId,
            createdAt = this.createdAt ?: System.currentTimeMillis(),
            changedAt =  this.changedAt ?: System.currentTimeMillis()
        )
    }

    fun getColorFromString(hexColor: String?): Color? {
        if (hexColor.isNullOrBlank()) return null

        val cleanHex = hexColor.removePrefix("#")
        val colorHex = if (cleanHex.length == 6) {
            "FF$cleanHex"
        } else {
            cleanHex
        }

        val colorInt = colorHex.toLong(16).toInt()
        return Color(colorInt)
    }

    fun colorToHexString(color: Color): String {
        val argb = color.toArgb()
        val rgb = argb and 0x00FFFFFF
        return String.format("#%06X", rgb)
    }
}

