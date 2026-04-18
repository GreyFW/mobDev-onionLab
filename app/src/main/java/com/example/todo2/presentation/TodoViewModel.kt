package com.example.todo2.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo2.domain.models.Task
import com.example.todo2.domain.usecases.ToggleTaskCompleteUseCase
import com.example.todo2.domain.usecases.todo.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class TodoViewModel(
    private val getTasksUseCase: GetTasksUseCase,
    private val toggleTaskCompleteUseCase: ToggleTaskCompleteUseCase,
    private val saveTaskTimeUseCase: SaveTaskTimeUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
) : ViewModel() {

    private val _allTasks = MutableStateFlow<List<Task>>(emptyList())
    val allTasks: StateFlow<List<Task>> = _allTasks.asStateFlow()

    val activeTasks = _allTasks.map { tasks -> tasks.filter { !it.completed } }
    val completedTasks = _allTasks.map { tasks -> tasks.filter { it.completed } }

    private val _selectedTask = MutableStateFlow<Task?>(null)
    val selectedTask: StateFlow<Task?> = _selectedTask.asStateFlow()

    fun loadTasks() {
        viewModelScope.launch {
            getTasksUseCase().onSuccess { tasks ->
                _allTasks.value = tasks
            }
        }
    }

    fun toggleTaskStatus(task: Task) {
        viewModelScope.launch {
            toggleTaskCompleteUseCase(task).onSuccess {
                loadTasks()
            }
        }
    }

    fun stopTimerAndSave(additionalSeconds: Int) {
        val currentTask = _selectedTask.value ?: return
        viewModelScope.launch {
            saveTaskTimeUseCase(currentTask, additionalSeconds).onSuccess {
                loadTasks()
            }
        }
    }

    fun selectTask(taskId: Int) {
        _selectedTask.value = _allTasks.value.find { it.id == taskId }
    }
}