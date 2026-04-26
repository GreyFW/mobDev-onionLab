package com.example.presentation_todo.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.domain_todo.models.Task
import com.example.presentation_todo.TodoViewModel

@Composable
fun TodoMinimalScreen(
    modifier: Modifier = Modifier,
    viewModel: TodoViewModel = hiltViewModel()
) {

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Тестовый экран для AppMetrica")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val fakeTask = Task(id = 1, title = "Сделать лабу", completed = false)
            viewModel.toggleTaskStatus(fakeTask)
        }) {
            Text("Переключить статус задачи")
        }
    }
}