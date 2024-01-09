package com.coolrinet.todolist.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.view.get
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.coolrinet.todolist.R
import com.coolrinet.todolist.data.Todo
import com.coolrinet.todolist.databinding.FragmentAddTodoBinding
import com.coolrinet.todolist.viewmodel.AddTodoViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddTodoFragment : Fragment() {
    private var _binding: FragmentAddTodoBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val viewModel: AddTodoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAddTodoBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            addTodoTitle.doOnTextChanged { text, _, _, _ ->
                viewModel.updateTodoState { oldTodo ->
                    oldTodo.copy(title = text.toString())
                }
            }

            addTodoPriority.setOnCheckedChangeListener { _, checkedId ->
                val newPriority = when (checkedId) {
                    addTodoPriorityHigh.id -> 1
                    addTodoPriorityMedium.id -> 2
                    addTodoPriorityLow.id -> 3
                    else -> 1
                }

                viewModel.updateTodoState { oldTodo ->
                    oldTodo.copy(priority = newPriority)
                }
            }

            addTodoConfirmButton.setOnClickListener {
                if (addTodoTitle.text.isNullOrBlank()) {
                    Snackbar.make(
                        view,
                        R.string.add_todo_title_empty,
                        Snackbar.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                viewModel.createTodo(
                    viewModel.todoState.value
                )

                findNavController().popBackStack()

                Snackbar.make(
                    view,
                    R.string.add_todo_success,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.todoState.collect { todo ->
                    updateUi(todo)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateUi(todo: Todo) {
        binding.apply {
            if (addTodoTitle.text.toString() != todo.title) {
                addTodoTitle.setText(todo.title)
            }

            val checkedButtonId = addTodoPriority.checkedRadioButtonId
            val checkedButton = addTodoPriority.findViewById<RadioButton>(checkedButtonId)

            if (addTodoPriority.indexOfChild(checkedButton) + 1 != todo.priority) {
                addTodoPriority.check(
                    addTodoPriority[todo.priority - 1].id
                )
            }
        }
    }
}