package com.example.domain_todo.usecases

import com.example.domain_todo.models.Task
import com.example.domain_todo.repository.ITodoRepository
import javax.inject.Inject

class ToggleTaskCompleteUseCase @Inject constructor(
    private val todoRepository: ITodoRepository
) {
    suspend operator fun invoke(task: Task): Result<Unit> {
        val updatedTask = task.copy(completed = !task.completed)
        return todoRepository.updateTask(updatedTask)
    }
}