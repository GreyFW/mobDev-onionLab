package com.example.domain.repositories

import com.example.domain.models.Task

interface ITodoRepository {
    suspend fun getTasks(): Result<List<Task>>
    suspend fun addTask(title: String): Result<Task>
    suspend fun updateTask(task: Task): Result<Unit>
    suspend fun deleteTask(id: Int): Result<Unit>
}