package com.example.todos.data.local.database.dao

import androidx.room.*
import com.example.todos.data.local.database.entity.TodoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Query("SELECT * FROM todos_items")
    fun getAll(): Flow<List<TodoEntity>>

    @Query("SELECT * FROM todos_items WHERE uid = :uid")
    suspend fun getById(uid: String): TodoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(todo: TodoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAll(todos: List<TodoEntity>)

    @Update
    suspend fun update(todo: TodoEntity)

    @Query("DELETE FROM todos_items WHERE uid = :uid")
    suspend fun deleteById(uid: String)
}