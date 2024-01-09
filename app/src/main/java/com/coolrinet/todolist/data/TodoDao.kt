package com.coolrinet.todolist.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Query("SELECT * FROM todos")
    fun getTodos(): Flow<List<Todo>>

    @Delete
    suspend fun deleteTodo(todo: Todo)

    @Insert
    suspend fun addTodo(todo: Todo)
}