package com.example.domain_todo.usecases

import com.example.domain_todo.models.Task
import com.example.domain_todo.repository.ITodoRepository
import javax.inject.Inject

class GetTasksUseCase @Inject constructor(
    private val repository: ITodoRepository
) {
    suspend operator fun invoke(): Result<List<Task>> {
        return repository.getTasks()
    }
}