package com.example.domain.usecases.todo

import com.example.domain.models.Task
import com.example.domain.repositories.ITodoRepository

class GetTasksUseCase(private val repository: ITodoRepository) {
    suspend operator fun invoke(): Result<List<Task>> {
        return repository.getTasks()
    }
}