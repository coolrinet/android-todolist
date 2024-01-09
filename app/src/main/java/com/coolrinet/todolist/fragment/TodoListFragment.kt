package com.coolrinet.todolist.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.coolrinet.todolist.R
import com.coolrinet.todolist.adapters.TodoListAdapter
import com.coolrinet.todolist.databinding.FragmentTodoListBinding
import com.coolrinet.todolist.viewmodel.TodoListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TodoListFragment : Fragment() {
    private var _binding: FragmentTodoListBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val viewModel: TodoListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTodoListBinding.inflate(inflater, container, false)

        binding.todoRecyclerView.layoutManager = LinearLayoutManager(context)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            addTodoFab.setOnClickListener {
                findNavController().navigate(
                    R.id.action_add_todo
                )
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.todos.collect { todos ->
                    val sortedTodos = todos.sortedBy { todo -> todo.priority }

                    binding.todoRecyclerView.adapter = TodoListAdapter(sortedTodos)

                    if (todos.isEmpty()) {
                        binding.todoRecyclerView.visibility = View.GONE
                        binding.todoListEmpty.visibility = View.VISIBLE
                    } else {
                        binding.todoRecyclerView.visibility = View.VISIBLE
                        binding.todoListEmpty.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}