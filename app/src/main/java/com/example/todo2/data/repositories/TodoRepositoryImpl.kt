package com.example.todo2.data.repositories

import com.example.todo2.domain.models.Task
import com.example.todo2.domain.repositories.ITodoRepository

class TodoRepositoryImpl(
    private val todoDao: TodoDao
) : ITodoRepository {

    override suspend fun getTasks(): Result<List<Task>> {
        return try {
            val entities = todoDao.getAllTasks()
            val domainTasks = entities.map { it.toDomainModel() } // Маппинг
            Result.success(domainTasks)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addTask(title: String, userId: Int): Result<Task> {
        return try {
            val newEntity = TaskEntity(title = title, userId = userId, completed = false)
            val id = todoDao.insertTask(newEntity)
            val domainTask = Task(id = id.toInt(), title = title, timeSpent = 0)
            Result.success(domainTask)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateTask(task: Task): Result<Unit> {
        return try {
            todoDao.updateTask(task.toEntityModel())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteTask(id: Int): Result<Unit> {
        return try {
            todoDao.deleteTaskById(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}