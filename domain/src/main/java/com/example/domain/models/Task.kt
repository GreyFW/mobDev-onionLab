package com.example.domain.models

data class Task(
    val id: Int,
    val title: String,
    val completed: Boolean = false,
    val subtasks: List<Subtask> = emptyList(),
    val notes: List<String> = emptyList(),
    val timeSpent: Int = 0
)