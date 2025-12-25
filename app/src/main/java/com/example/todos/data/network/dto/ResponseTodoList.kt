package com.example.todos.data.network.dto

import com.google.gson.annotations.SerializedName

data class ResponseTodoList(
    @SerializedName("status")
    val status: String,

    @SerializedName("list")
    val list: List<TodoItemDTO>,

    @SerializedName("revision")
    val revision: Int
)
