package com.kvork_app.diplomawork.view.ui.navigation

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object Requests : Screen("requests")
    object AddRequest : Screen("add_request")
    object EditRequest : Screen("edit_request")
}