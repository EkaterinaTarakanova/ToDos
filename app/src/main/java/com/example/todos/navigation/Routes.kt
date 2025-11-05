package com.example.todos.navigation

sealed class Routes(val route: String) {
    data object EditScreen : Routes("edit")
    data object ColorPicker : Routes("color")
}