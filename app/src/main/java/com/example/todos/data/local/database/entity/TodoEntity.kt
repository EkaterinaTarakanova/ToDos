package com.example.todos.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todos_items")
data class TodoEntity(
    @PrimaryKey
    @ColumnInfo(name = "uid")
    val uid: String,

    @ColumnInfo(name = "text")
    val text: String,

    @ColumnInfo(name = "importance")
    val importance: String,

    @ColumnInfo(name = "task_color")
    val taskColor: String?,

    @ColumnInfo(name = "deadline")
    val deadline: Long,

    @ColumnInfo(name = "is_done")
    val isDone: Boolean,

    @ColumnInfo(name = "created_at")
    val createdAt: Long?,

    @ColumnInfo(name = "changed_at")
    val changedAt: Long?,
)