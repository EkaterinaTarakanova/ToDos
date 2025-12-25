package com.example.todos

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.todos.ui.theme.ToDosTheme
import ch.qos.logback.classic.android.BasicLogcatConfigurator;
import com.example.todos.data.local.FileStorage
import com.example.todos.data.network.RemoteServer
import com.example.todos.data.storage.DeviceIdStorage
import com.example.todos.data.storage.RevisionStorage
import com.example.todos.presentation.navigation.NavigationGraph
import com.example.todos.data.repository.TodoRepository
import com.example.todos.presentation.screens.LoginScreen
import androidx.core.content.edit
import com.example.todos.data.local.database.AppDatabase
import com.example.todos.data.local.database.datastorage.RoomDataStorage

class MainActivity : ComponentActivity() {
    init {
        BasicLogcatConfigurator.configureDefaultContext()
    }
    private lateinit var remoteServer: RemoteServer
    private lateinit var database: AppDatabase
    private lateinit var roomDataStorage: RoomDataStorage
    private lateinit var todoRepository: TodoRepository
    private lateinit var yandexAuthLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        yandexAuthLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            handleYandexAuthResult(result)
        }

        val prefs = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        prefs.edit { remove("oauth_token") }

        remoteServer = RemoteServer(applicationContext)
        database = AppDatabase.getInstance(applicationContext)
        roomDataStorage = RoomDataStorage(database)
        todoRepository = TodoRepository(
            remoteServer = remoteServer,
            context = applicationContext,
            roomDataStorage = roomDataStorage
        )

        showLoginChoice()
    }

    private fun showLoginChoice() {
        setContent {
            LoginScreen(
                onYandexClick = {
                    val intent = Intent(this, YandexOAuthActivity::class.java)
                    yandexAuthLauncher.launch(intent)
                },
                onContinueClick = {
                    startMainApp()
                }
            )
        }
    }

    private fun handleYandexAuthResult(result: ActivityResult) {
        when (result.resultCode) {
            RESULT_OK -> {
                val data = result.data
                val yandexToken = data?.getStringExtra("TOKEN")
                if (!yandexToken.isNullOrEmpty()) {
                    val prefs = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
                    prefs.edit { putString("oauth_token", yandexToken) }

                    startMainApp()
                }
            }
        }
    }

    private fun startMainApp() {
        DeviceIdStorage.init(applicationContext)
        RevisionStorage.init(applicationContext)

        setContent {
            ToDosTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFE0F2FE))
                )
                NavigationGraph(
                    todoRepository,
                    context = applicationContext
                )
            }
        }
    }
}