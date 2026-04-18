package com.example.todo2.domain.usecases.todo

import com.example.todo2.domain.models.Task
import com.example.todo2.domain.repositories.ITodoRepository

class GetTasksUseCase(private val repository: ITodoRepository) {
    suspend operator fun invoke(): Result<List<Task>> {
        return repository.getTasks()
    }
}