package com.example.todos.data.network.dto

import com.google.gson.annotations.SerializedName

data class ResponseTodoElement(
    @SerializedName("status")
    val status: String,

    @SerializedName("element")
    val element: TodoItemDTO,

    @SerializedName("revision")
    val revision: Int,
)
