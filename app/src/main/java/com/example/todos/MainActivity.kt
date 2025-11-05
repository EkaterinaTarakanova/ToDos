package com.example.todos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.todos.ui.theme.ToDosTheme
import ch.qos.logback.classic.android.BasicLogcatConfigurator;
import com.example.todos.navigation.NavigationGraph

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
                NavigationGraph()
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

