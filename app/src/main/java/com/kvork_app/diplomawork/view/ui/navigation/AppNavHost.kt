package com.kvork_app.diplomawork.view.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kvork_app.diplomawork.view.ui.screens.ActionStatisticsScreen
import com.kvork_app.diplomawork.view.ui.screens.AddRequestScreen
import com.kvork_app.diplomawork.view.ui.screens.AddressStatsScreen
import com.kvork_app.diplomawork.view.ui.screens.ChangesApplicationScreen
import com.kvork_app.diplomawork.view.ui.screens.MainScreenConstraint
import com.kvork_app.diplomawork.view.ui.screens.RequestActionsScreen
import com.kvork_app.diplomawork.view.ui.screens.StatisticsScreen
import com.kvork_app.diplomawork.view.ui.screens.ViewRequestsScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Main.route) {
        composable(Screen.Main.route) {
            MainScreenConstraint(
                onNavigateToRequests = { navController.navigate(Screen.Requests.route) },
                onNavigateToStats = { navController.navigate(Screen.AllStats.route) }
            )
        }

        composable(Screen.Requests.route) {
            RequestActionsScreen(
                navController = navController,
                onBackClick = { navController.popBackStack() },
                onAddRequest = { navController.navigate(Screen.AddRequest.route) },
                onEditRequest = { navController.navigate(Screen.EditRequest.route) },
                onViewRequests = { navController.navigate(Screen.ViewRequest.route) }
            )
        }
        composable(Screen.AllStats.route) {
            ActionStatisticsScreen(
                navController = navController,
                onBackClick = {navController.popBackStack()},
                onMaterialStatistics = {navController.navigate(Screen.Statistics.route)},
                onAddressStatistics = {navController.navigate(Screen.AddressStats.route)}
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
        composable(Screen.ViewRequest.route) {
            ViewRequestsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        composable (Screen.Statistics.route) {
            StatisticsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        composable (Screen.AddressStats.route) {
            AddressStatsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }

}