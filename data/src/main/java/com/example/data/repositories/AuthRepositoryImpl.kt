package com.example.data.repositories

import com.example.domain.models.User
import com.example.domain.repositories.IAuthRepository

interface UserDao {
    suspend fun getUserByEmail(email: String): UserEntity?
    suspend fun insertUser(user: UserEntity): Long
}

interface UserPreferences {
    suspend fun saveCurrentUser(user: User)
    suspend fun clearCurrentUser()
    suspend fun getCurrentUser(): User?
}

data class UserEntity(
    val id: String,
    val email: String,
    val password: String
)

class AuthRepositoryImpl(
    private val userDao: UserDao,
    private val userPreferences: UserPreferences
) : IAuthRepository {

    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            val userEntity = userDao.getUserByEmail(email)

            if (userEntity == null) {
                return Result.failure(Exception("Нет пользователя с таким email"))
            }

            if (userEntity.password != password) {
                return Result.failure(Exception("Неверный пароль"))
            }

            val user = User(id = userEntity.id, email = userEntity.email)

            userPreferences.saveCurrentUser(user)

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(email: String, password: String): Result<User> {
        return try {
            val existingUser = userDao.getUserByEmail(email)
            if (existingUser != null) {
                return Result.failure(Exception("Пользователь с таким email уже зарегистрирован"))
            }

            val newId = System.currentTimeMillis().toString()
            val newUserEntity = UserEntity(id = newId, email = email, password = password)

            userDao.insertUser(newUserEntity)

            val user = User(id = newId, email = email)

            userPreferences.saveCurrentUser(user)

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            userPreferences.clearCurrentUser()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}