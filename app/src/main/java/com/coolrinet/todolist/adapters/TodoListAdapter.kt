package com.coolrinet.todolist.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.coolrinet.todolist.R
import com.coolrinet.todolist.data.Todo
import com.coolrinet.todolist.databinding.ListItemTodoBinding

class TodoHolder(
    private val binding: ListItemTodoBinding,
) : RecyclerView.ViewHolder(binding.root) {
    private val priorities: Map<Int, Int> = mapOf(
        1 to R.color.priority_high,
        2 to R.color.priority_medium,
        3 to R.color.priority_low
    )

    fun bind(todo: Todo) {
        binding.todoItemTitle.text = todo.title

        binding.todoItemPriority.text = todo.priority.toString()

        val priorityBackground = binding.todoItemPriority.background
        val context = itemView.context

        priorityBackground.setTint(
            ContextCompat.getColor(
                context,
                priorities.getValue(todo.priority)
            )
        )

        binding.todoItemPriority.background = priorityBackground
    }
}

class TodoListAdapter(
    private val todos: List<Todo>,
) : RecyclerView.Adapter<TodoHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemTodoBinding.inflate(inflater, parent, false)
        return TodoHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoHolder, position: Int) {
        val todo = todos[position]
        holder.bind(todo)
    }

    override fun getItemCount() = todos.size
}