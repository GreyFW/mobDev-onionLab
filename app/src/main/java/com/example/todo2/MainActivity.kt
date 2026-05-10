package com.example.todo2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.presentation_auth.ui.AuthScreen
import com.example.todo2.ui.theme.ToDo2Theme
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        MapKitFactory.setApiKey("d2d46cea-def8-4ae6-a5c7-adae8e953f43")
        MapKitFactory.initialize(this)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ToDo2Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AuthScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}