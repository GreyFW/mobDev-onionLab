package com.example.domain_todo.usecases

import com.example.domain_todo.repository.ITodoRepository

class DeleteTaskUseCase(private val repository: ITodoRepository) {
    suspend operator fun invoke(id: Int): Result<Unit> {
        return repository.deleteTask(id)
    }
}