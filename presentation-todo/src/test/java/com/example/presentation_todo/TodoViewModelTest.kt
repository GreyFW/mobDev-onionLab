package com.example.presentation_todo

import com.example.core.analytics.FakeAnalyticsService
import com.example.domain_todo.models.Task
import com.example.domain_todo.repository.ITodoRepository
import com.example.domain_todo.usecases.DeleteTaskUseCase
import com.example.domain_todo.usecases.GetTasksUseCase
import com.example.domain_todo.usecases.SaveTaskTimeUseCase
import com.example.domain_todo.usecases.ToggleTaskCompleteUseCase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class TodoViewModelTest {

    private lateinit var fakeAnalytics: FakeAnalyticsService
    private lateinit var viewModel: TodoViewModel

    private val fakeRepository = object : ITodoRepository {
        override suspend fun getTasks() = Result.success(emptyList<Task>())
        override suspend fun addTask(title: String) = Result.success(Task(1, title))
        override suspend fun updateTask(task: Task) = Result.success(Unit)
        override suspend fun deleteTask(id: Int) = Result.success(Unit)
    }

    @Before
    fun setup() {
        fakeAnalytics = FakeAnalyticsService()

        viewModel = TodoViewModel(
            getTasksUseCase = GetTasksUseCase(fakeRepository),
            toggleTaskCompleteUseCase = ToggleTaskCompleteUseCase(fakeRepository),
            saveTaskTimeUseCase = SaveTaskTimeUseCase(fakeRepository),
            deleteTaskUseCase = DeleteTaskUseCase(fakeRepository),
            analytics = fakeAnalytics
        )
    }

    @Test
    fun `when viewmodel is created, screen_viewed event is sent`() {
        val event = fakeAnalytics.loggedEvents.find { it.first == "screen_viewed" }

        assertTrue("Событие screen_viewed не найдено", event != null)
        assertEquals("todo_main_screen", event?.second?.get("screen_name"))
    }

    @Test
    fun `when task toggled, task_status_toggled event is sent`() {
        val testTask = Task(id = 99, title = "Тестовая задача", completed = false)

        viewModel.toggleTaskStatus(testTask)

        val event = fakeAnalytics.loggedEvents.find { it.first == "task_status_toggled" }

        assertTrue("Событие task_status_toggled не найдено", event != null)
        assertEquals(99, event?.second?.get("task_id"))
        assertEquals(true, event?.second?.get("new_status"))
    }
}