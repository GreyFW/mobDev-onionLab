package com.example.presentation_todo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.analytics.AnalyticsService
import com.example.domain_todo.models.Task
import com.example.domain_todo.usecases.DeleteTaskUseCase
import com.example.domain_todo.usecases.GetTasksUseCase
import com.example.domain_todo.usecases.SaveTaskTimeUseCase
import com.example.domain_todo.usecases.ToggleTaskCompleteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val getTasksUseCase: GetTasksUseCase,
    private val toggleTaskCompleteUseCase: ToggleTaskCompleteUseCase,
    private val saveTaskTimeUseCase: SaveTaskTimeUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val analytics: AnalyticsService
) : ViewModel() {

    private val _allTasks = MutableStateFlow<List<Task>>(emptyList())
    val allTasks: StateFlow<List<Task>> = _allTasks.asStateFlow()

    val activeTasks = _allTasks.map { tasks -> tasks.filter { !it.completed } }
    val completedTasks = _allTasks.map { tasks -> tasks.filter { it.completed } }

    private val _selectedTask = MutableStateFlow<Task?>(null)
    val selectedTask: StateFlow<Task?> = _selectedTask.asStateFlow()

    init {
        analytics.trackEvent(
            name = "screen_viewed",
            params = mapOf("screen_name" to "todo_main_screen")
        )
    }

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
                analytics.trackEvent(
                    name = "task_status_toggled",
                    params = mapOf(
                        "task_id" to task.id,
                        "new_status" to !task.completed
                    )
                )
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