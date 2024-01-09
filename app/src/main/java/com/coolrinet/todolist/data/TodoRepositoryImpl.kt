package com.coolrinet.todolist.data

class TodoRepositoryImpl(
    private val dao: TodoDao,
) : TodoRepository {
    override fun getTodos() = dao.getTodos()

    override suspend fun deleteTodo(todo: Todo) {
        dao.deleteTodo(todo)
    }

    override suspend fun addTodo(todo: Todo) {
        dao.addTodo(todo)
    }
}