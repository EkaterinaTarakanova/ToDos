package com.example.todos.data.network.dto

import com.google.gson.annotations.SerializedName

data class TodoElementRequest(
    @SerializedName("element")
    val element: TodoItemDTO
)
