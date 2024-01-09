package com.coolrinet.todolist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coolrinet.todolist.data.Todo
import com.coolrinet.todolist.data.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTodoViewModel @Inject constructor(
    private val todoRepository: TodoRepository,
) : ViewModel() {
    private val _todoState: MutableStateFlow<Todo> = MutableStateFlow(
        Todo(title = "", priority = 1)
    )
    val todoState: StateFlow<Todo> = _todoState.asStateFlow()

    fun updateTodoState(onUpdate: (Todo) -> Todo) {
        _todoState.update { oldTodo ->
            oldTodo.let(onUpdate)
        }
    }

    fun createTodo(todo: Todo) {
        viewModelScope.launch {
            todoRepository.addTodo(todo)
        }
    }

}