package com.kvork_app.diplomawork.view.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kvork_app.diplomawork.view.ui.screens.MainScreenConstraint
import com.kvork_app.diplomawork.view.ui.screens.RequestActionsScreen

@Composable
fun AppNavHost(navController: NavHostController) {

    NavHost(navController = navController, startDestination = Screen.Main.route) {
        composable(Screen.Main.route) {
            MainScreenConstraint(
                onNavigateToRequests = { navController.navigate(Screen.Requests.route) },
                onNavigateToStats = { /* TODO: Добавить экран статистики */ }
            )
        }
        composable(Screen.Requests.route) {
            RequestActionsScreen(navController)
        }
    }

}