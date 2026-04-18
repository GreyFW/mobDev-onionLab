package com.example.domain.usecases

import com.example.domain.models.Task
import com.example.domain.repositories.ITodoRepository

class ToggleTaskCompleteUseCase(
    private val todoRepository: ITodoRepository
) {
    suspend operator fun invoke(task: Task): Result<Unit> {
        val updatedTask = task.copy(completed = !task.completed)
        return todoRepository.updateTask(updatedTask)
    }
}