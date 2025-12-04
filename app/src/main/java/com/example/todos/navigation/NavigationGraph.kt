package com.example.todos.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.toArgb
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todos.viewModels.EditTaskViewModelFactory
import com.example.todos.data.FileStorage
import com.example.todos.viewModels.TaskListViewModelFactory
import com.example.todos.screens.ColorPickerScreen
import com.example.todos.screens.EditTaskScreen
import com.example.todos.screens.TaskListScreen

@Composable
fun NavigationGraph(fileStorage: FileStorage) {
    val navController = rememberNavController()
    val savedColor = remember { mutableStateOf<Color?>(null) }
    val savedPosition = remember { mutableStateOf<Offset?>(null) }

    val taskListViewModelFactory = TaskListViewModelFactory(fileStorage)

    NavHost(navController = navController, startDestination = Routes.ListScreen.route) {
        composable(Routes.ListScreen.route) {

            TaskListScreen(
                onAddTaskClick = {
                    navController.navigate(Routes.EditScreen.route + "/new")
                },
                taskListViewModelFactory = taskListViewModelFactory,
                onTaskClick = { taskId -> navController.navigate(Routes.EditScreen.route + "/$taskId") })
        }

        composable(Routes.EditScreen.route + "/{todoId}") { stackEntry ->
            val todoId = stackEntry.arguments?.getString("todoId")
            val editTaskViewModelFactory = EditTaskViewModelFactory(fileStorage, todoId ?: "new", stackEntry.savedStateHandle)

            EditTaskScreen(
                onColorPickerClick = {
                    navController.navigate(Routes.ColorPicker.route)
                },
                onSaveTask = {
                    navController.popBackStack()
                },
                editTaskViewModelFactory = editTaskViewModelFactory,
            )
        }

        composable(
            Routes.ColorPicker.route, enterTransition = {
                slideInVertically(
                    initialOffsetY = { fullHeight -> fullHeight },
                    animationSpec = tween(durationMillis = 500)
                ) + fadeIn(animationSpec = tween(durationMillis = 500))
            },
            exitTransition = {
                slideOutVertically(
                    targetOffsetY = { fullHeight -> fullHeight },
                    animationSpec = tween(durationMillis = 500)
                ) + fadeOut(animationSpec = tween(durationMillis = 500))
            }) {
            ColorPickerScreen(
                onClick = { selectedColor, selectedPosition ->
                    savedColor.value = selectedColor
                    savedPosition.value = selectedPosition
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("selectedColor", selectedColor.toArgb())
                    navController.popBackStack()
                },
                initialColor = savedColor.value ?: Color.Red,
                initialPosition = savedPosition.value
            )
        }
    }
}
