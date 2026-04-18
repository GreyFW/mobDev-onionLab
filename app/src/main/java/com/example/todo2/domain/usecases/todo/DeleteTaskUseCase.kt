package com.example.todo2.domain.usecases.todo

import com.example.todo2.domain.repositories.ITodoRepository

class DeleteTaskUseCase(private val repository: ITodoRepository) {
    suspend operator fun invoke(id: Int): Result<Unit> {
        return repository.deleteTask(id)
    }
}