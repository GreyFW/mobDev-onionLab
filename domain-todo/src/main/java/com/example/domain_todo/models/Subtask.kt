package com.example.domain_todo.models

data class Subtask(
    val text: String,
    val completed: Boolean = false
)