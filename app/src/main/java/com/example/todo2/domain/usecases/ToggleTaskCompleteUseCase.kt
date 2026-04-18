package com.example.todo2.domain.usecases

import com.example.todo2.domain.models.Task
import com.example.todo2.domain.repositories.ITodoRepository

class ToggleTaskCompleteUseCase(
    private val todoRepository: ITodoRepository
) {
    suspend operator fun invoke(task: Task): Result<Unit> {
        val updatedTask = task.copy(completed = !task.completed)
        return todoRepository.updateTask(updatedTask)
    }
}