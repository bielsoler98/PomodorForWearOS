package com.senyor_o.pomodoro.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.senyor_o.pomodoro.presentation.settings.SettingsScreen
import com.senyor_o.pomodoro.screen.ChangeDurationScreen
import com.senyor_o.pomodoro.screen.MainScreen

@Composable
fun AppNavigation() {
    val swipeDismissibleNavController = rememberSwipeDismissableNavController()
    val context = LocalContext.current
    SwipeDismissableNavHost(
        navController = swipeDismissibleNavController,
        startDestination = AppScreens.MainScreen.route,
        modifier = Modifier.background(MaterialTheme.colors.background)
    ) {
        composable(route =  AppScreens.MainScreen.route) {
            MainScreen(swipeDismissibleNavController)
        }
        composable(route =  AppScreens.SettingsScreen.route) {
            SettingsScreen(swipeDismissibleNavController)
        }
        composable(
            route = AppScreens.ChangeDurationScreen.route,
            arguments = listOf(navArgument("id") {
                type = NavType.StringType
            })
        ) {
            ChangeDurationScreen(
                swipeDismissibleNavController
            )
        }
    }
}