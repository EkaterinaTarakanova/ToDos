package com.example.todos.data.local.database.mappers

import com.example.todos.data.local.database.entity.TodoEntity
import com.example.todos.data.mapper.TodoMapper.colorToHexString
import com.example.todos.data.mapper.TodoMapper.getColorFromString
import com.example.todos.domain.model.Importance
import com.example.todos.domain.model.TodoItem
import org.joda.time.DateTime

fun TodoItem.toEntity(): TodoEntity {
    return TodoEntity(
        uid = this.uid,
        text = this.text,
        importance = this.importance.name,
        taskColor = this.color?.let { colorToHexString(it) },
        deadline = this.deadline?.millis ?: 0,
        isDone = this.isDone,
        createdAt = this.createdAt,
        changedAt = this.changedAt,
    )
}

fun TodoEntity.toDomain(): TodoItem {
    return TodoItem(
        uid = this.uid,
        text = this.text,
        importance = Importance.valueOf(this.importance),
        color = getColorFromString(this.taskColor),
        deadline = if (this.deadline > 0) DateTime(this.deadline) else null,
        isDone = this.isDone,
        createdAt = this.createdAt,
        changedAt = this.changedAt
    )
}