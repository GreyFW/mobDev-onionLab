package com.example.presentation_auth.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.presentation_auth.AuthState
import com.example.presentation_auth.LoginViewModel

@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val authState by viewModel.authState.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        viewModel.handleLoginResult(result.resultCode, result.data)
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (authState) {
            is AuthState.Idle, is AuthState.Error -> {
                if (authState is AuthState.Error) {
                    Text(
                        text = "Ошибка: ${(authState as AuthState.Error).message}",
                        color = Color.Red
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Text("Необходима авторизация")
                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    launcher.launch(viewModel.getLoginIntent())
                }) {
                    Text("Войти через Яндекс")
                }
            }
            is AuthState.Loading -> {
                Text("Загрузка...")
            }
            is AuthState.Authenticated -> {
                val userName = (authState as AuthState.Authenticated).userName
                Text("Добро пожаловать, $userName!")
                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Вы успешно авторизованы", color = Color.Green)

                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { viewModel.logout() }) {
                    Text("Выйти")
                }
            }
        }
    }
}