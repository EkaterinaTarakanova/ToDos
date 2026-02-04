package com.example.todos.data.network.dto

import com.google.gson.annotations.SerializedName

data class TodoListRequest(
    @SerializedName("list")
    val list: List<TodoItemDTO>
)
