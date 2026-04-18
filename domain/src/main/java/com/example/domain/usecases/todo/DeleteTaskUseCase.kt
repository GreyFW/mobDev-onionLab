package com.example.domain.usecases.todo

import com.example.domain.repositories.ITodoRepository

class DeleteTaskUseCase(private val repository: ITodoRepository) {
    suspend operator fun invoke(id: Int): Result<Unit> {
        return repository.deleteTask(id)
    }
}