package com.example.todos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.todos.ui.theme.ToDosTheme
import ch.qos.logback.classic.android.BasicLogcatConfigurator;
import org.joda.time.DateTime

class MainActivity : ComponentActivity() {
    companion object {
        init {
            BasicLogcatConfigurator.configureDefaultContext()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ToDosTheme {
                val fileStorage = FileStorage(this)

                val testItem1 = TodoItem(
                    uid = "test-uid-1",
                    text = "Купить молоко",
                    importance = Importance.IMPORTANT,
                    color = Color.Blue,
                    deadline = DateTime.now().plusDays(2)
                )

                val testItem2 = TodoItem(
                    text = "Сделать домашку",
                    importance = Importance.ORDINARY,
                    color = Color.White,
                    deadline = DateTime.now().plusHours(8)
                )

                fileStorage.addNewTodo(testItem1)
                fileStorage.addNewTodo(testItem2)

                val newFileStorage = FileStorage(this)
                newFileStorage.loadTodosFromFile()

                newFileStorage.deleteTodo("test-uid-1")
            }
        }
    }
}

    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        ToDosTheme {
            Greeting("Android")
        }
    }

