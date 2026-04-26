package com.example.domain_todo.usecases

import com.example.domain_todo.repository.ITodoRepository
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(
    private val repository: ITodoRepository
) {
    suspend operator fun invoke(id: Int): Result<Unit> {
        return repository.deleteTask(id)
    }
}