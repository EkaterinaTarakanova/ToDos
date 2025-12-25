package com.example.todos.presentation.navigation

sealed class Routes(val route: String) {
    data object ListScreen : Routes("list")
    data object EditScreen : Routes("edit")
    data object ColorPicker : Routes("color")
}