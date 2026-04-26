package com.example.domain_todo.usecases

import com.example.domain_todo.models.Task
import com.example.domain_todo.repository.ITodoRepository
import javax.inject.Inject

class SaveTaskTimeUseCase @Inject constructor(
    private val todoRepository: ITodoRepository
) {
    suspend operator fun invoke(task: Task, additionalSeconds: Int): Result<Unit> {
        if (additionalSeconds <= 0) return Result.success(Unit)

        val updatedTask = task.copy(timeSpent = task.timeSpent + additionalSeconds)
        return todoRepository.updateTask(updatedTask)
    }
}