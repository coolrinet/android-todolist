package com.coolrinet.todolist.data

import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    fun getTodos(): Flow<List<Todo>>

    suspend fun deleteTodo(todo: Todo)

    suspend fun addTodo(todo: Todo)
}