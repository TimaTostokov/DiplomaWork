package com.kvork_app.diplomawork.view.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kvork_app.diplomawork.view.ui.screens.AddRequestScreen
import com.kvork_app.diplomawork.view.ui.screens.ChangesApplicationScreen
import com.kvork_app.diplomawork.view.ui.screens.MainScreenConstraint
import com.kvork_app.diplomawork.view.ui.screens.RequestActionsScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Main.route) {
        composable(Screen.Main.route) {
            MainScreenConstraint(
                onNavigateToRequests = { navController.navigate(Screen.Requests.route) },
                onNavigateToStats = { /* Пока не реализовано */ }
            )
        }

        composable(Screen.Requests.route) {
            RequestActionsScreen(
                navController = navController,
                onBackClick = { navController.popBackStack() },
                onAddRequest = { navController.navigate(Screen.AddRequest.route) },
                onEditRequest = { navController.navigate(Screen.EditRequest.route) },
                onViewRequests = { /* Здесь можно обрабатывать просмотр заявок */ }
            )
        }

        composable(Screen.AddRequest.route) {
            AddRequestScreen(
                onBackClick = { navController.popBackStack() },
                onSubmit = { navController.popBackStack() }
            )
        }

        composable(Screen.EditRequest.route) {
            ChangesApplicationScreen(
                onBackClick = { navController.popBackStack() },
                onUpdate = { navController.popBackStack() }
            )
        }
    }

}