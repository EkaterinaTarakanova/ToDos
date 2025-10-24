package com.example.todos.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todos.screens.ColorPickerScreen
import com.example.todos.screens.EditTaskScreen

@Composable
fun NavigationGraph() {
    val navController = rememberNavController()
    val savedColor = remember { mutableStateOf<Color?>(null) }
    val savedPosition = remember { mutableStateOf<Offset?>(null) }

    NavHost(navController = navController, startDestination = Routes.EditScreen.route) {
        composable(Routes.EditScreen.route) {
            EditTaskScreen(onColorPickerClick = {
                navController.navigate(Routes.ColorPicker.route)
            }, customColor = savedColor.value)
        }

        composable(Routes.ColorPicker.route) {
            ColorPickerScreen(
                onClick = { selectedColor, selectedPosition ->
                    savedColor.value = selectedColor
                    savedPosition.value = selectedPosition
                    navController.popBackStack()
                },
                initialColor = savedColor.value ?: Color.Red,
                initialPosition = savedPosition.value
            )
        }
    }
}
