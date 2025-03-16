package com.kvork_app.diplomawork.view.ui.navigation

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object Requests : Screen("requests")
    object AllStats: Screen("stats")
    object AddRequest : Screen("add_request")
    object EditRequest : Screen("edit_request")
    object ViewRequest : Screen("view_request")
    object Statistics : Screen("statistics")
    object AddressStats : Screen("address_stats")

}