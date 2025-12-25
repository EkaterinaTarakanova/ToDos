package com.example.todos.presentation.navigation

import android.content.Context
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.toArgb
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todos.presentation.viewModels.EditTaskViewModelFactory
import com.example.todos.data.repository.TodoRepository
import com.example.todos.presentation.viewModels.TaskListViewModelFactory
import com.example.todos.presentation.screens.ColorPickerScreen
import com.example.todos.presentation.screens.EditTaskScreen
import com.example.todos.presentation.screens.TaskListScreen

@Composable
fun NavigationGraph(
    todoRepository: TodoRepository,
    context: Context
) {
    val navController = rememberNavController()
    val savedColor = remember { mutableStateOf<Color?>(null) }

    val taskListViewModelFactory = TaskListViewModelFactory(todoRepository)

    NavHost(navController = navController, startDestination = Routes.ListScreen.route
    ) {
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
            val editTaskViewModelFactory = EditTaskViewModelFactory(
                todoRepository,
                todoId ?: "new",
                stackEntry.savedStateHandle
            )

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
                onClick = { selectedColor ->
                    savedColor.value = selectedColor
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("selectedColor", selectedColor.toArgb())
                    navController.popBackStack()
                },
                initialColor = savedColor.value ?: Color.Red,
            )
        }
    }
}
