package com.example.todos.data.network

import com.example.todos.data.network.dto.ResponseTodoElement
import com.example.todos.data.network.dto.ResponseTodoList
import com.example.todos.data.network.dto.TodoElementRequest
import com.example.todos.data.network.dto.TodoListRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @GET("list")
    suspend fun getTodos(): Response<ResponseTodoList>

    @PATCH("list")
    @Headers("Content-Type: application/json")
    suspend fun updateTodos(
        @Header("X-Last-Known-Revision") revision: Int,
        @Body request: TodoListRequest
    ): Response<ResponseTodoList>

    @GET("list/{id}")
    suspend fun getTodo(@Path("id") todoId: String): Response<ResponseTodoElement>

    @POST("list")
    suspend fun addTodo(
        @Header("X-Last-Known-Revision") revision: Int,
        @Body element: TodoElementRequest
    ): Response<ResponseTodoElement>

    @PUT("list/{id}")
    suspend fun updateTodo(
        @Path("id") todoId: String,
        @Header("X-Last-Known-Revision") revision: Int,
        @Body element: TodoElementRequest
    ): Response<ResponseTodoElement>

    @DELETE("list/{id}")
    suspend fun deleteTodo(
        @Path("id") todoId: String,
        @Header("X-Last-Known-Revision") revision: Int,
    ): Response<ResponseTodoElement>
}