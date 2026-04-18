package com.example.domain_auth

class RegisterUseCase(
    private val authRepository: IAuthRepository
) {
    suspend operator fun invoke(email: String, password: String, confirmPass: String): Result<User> {
        if (email.isBlank() || password.isBlank()) {
            return Result.failure(Exception("Заполните все поля"))
        }
        if (password.length < 6) {
            return Result.failure(Exception("Пароль слишком короткий"))
        }
        if (password != confirmPass) {
            return Result.failure(Exception("Пароли не совпадают"))
        }
        return authRepository.register(email, password)
    }
}