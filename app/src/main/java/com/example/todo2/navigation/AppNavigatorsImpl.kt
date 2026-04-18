package com.example.todo2.navigation

import androidx.navigation.NavController
import com.example.core.navigation.AuthNavigator
import com.example.core.navigation.TodoNavigator

class AuthNavigatorImpl(private val navController: NavController) : AuthNavigator {
    override fun navigateToTodo() {
        // Логика перехода в Jetpack Compose Navigation
        navController.navigate("todo_screen") {
            popUpTo("auth_screen") { inclusive = true }
        }
    }
}