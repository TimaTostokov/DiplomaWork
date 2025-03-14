package com.kvork_app.diplomawork.view.ui.navigation

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object Requests : Screen("requests")
}