package com.example.domain.usecases.todo

import com.example.domain.models.Task
import com.example.domain.repositories.ITodoRepository

class SaveTaskTimeUseCase(
    private val todoRepository: ITodoRepository
) {
    suspend operator fun invoke(task: Task, additionalSeconds: Int): Result<Unit> {
        if (additionalSeconds <= 0) return Result.success(Unit)

        val updatedTask = task.copy(timeSpent = task.timeSpent + additionalSeconds)
        return todoRepository.updateTask(updatedTask)
    }
}