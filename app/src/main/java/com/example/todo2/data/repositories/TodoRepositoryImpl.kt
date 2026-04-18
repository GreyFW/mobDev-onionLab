package com.example.todo2.data.repositories

import com.example.todo2.domain.models.Task
import com.example.todo2.domain.repositories.ITodoRepository
import com.example.todo2.domain.models.Subtask
import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val completed: Boolean,
    val subtasksJson: String,
    val notesJson: String,
    val timeSpent: Int
)

class RoomConverters {
    private val gson = Gson()

    @TypeConverter
    fun fromSubtaskList(value: List<Subtask>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toSubtaskList(value: String): List<Subtask> {
        val type = object : TypeToken<List<Subtask>>() {}.type
        return gson.fromJson(value, type) ?: emptyList()
    }

    @TypeConverter
    fun fromNotesList(value: List<String>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toNotesList(value: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, type) ?: emptyList()
    }
}

@Dao
interface TodoDao {
    @Query("SELECT * FROM tasks")
    suspend fun getAllTasks(): List<TaskEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity): Long

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteTaskById(id: Int)
}

class TodoRepositoryImpl(
    private val todoDao: TodoDao,
    private val converters: RoomConverters
) : ITodoRepository {

    private fun TaskEntity.toDomainModel(): Task {
        return Task(
            id = this.id,
            title = this.title,
            completed = this.completed,
            subtasks = converters.toSubtaskList(this.subtasksJson),
            notes = converters.toNotesList(this.notesJson),
            timeSpent = this.timeSpent
        )
    }

    private fun Task.toEntityModel(): TaskEntity {
        return TaskEntity(
            id = this.id,
            title = this.title,
            completed = this.completed,
            subtasksJson = converters.fromSubtaskList(this.subtasks),
            notesJson = converters.fromNotesList(this.notes),
            timeSpent = this.timeSpent
        )
    }

    override suspend fun getTasks(): Result<List<Task>> {
        return try {
            val entities = todoDao.getAllTasks()
            val domainTasks = entities.map { it.toDomainModel() }
            Result.success(domainTasks)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addTask(title: String): Result<Task> {
        return try {
            val newEntity = TaskEntity(
                title = title,
                completed = false,
                subtasksJson = "[]",
                notesJson = "[]",
                timeSpent = 0
            )
            val insertedId = todoDao.insertTask(newEntity)

            val domainTask = Task(
                id = insertedId.toInt(),
                title = title,
                subtasks = emptyList(),
                notes = emptyList(),
                timeSpent = 0
            )
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