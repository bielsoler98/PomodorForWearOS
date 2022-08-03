package com.example.pomodoro.navigation

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.wear.compose.navigation.composable
import com.example.pomodoro.database.SettingsViewModel
import com.example.pomodoro.database.SettingsViewModelFactory
import com.example.pomodoro.screen.ChangeDurationScreen
import com.example.pomodoro.screen.MainScreen
import com.example.pomodoro.screen.SettingsScreen
import com.example.pomodoro.screen.SplashScreen
import com.example.pomodoro.utils.Utility

@Composable
fun AppNavigation() {
    val swipeDismissibleNavController = rememberSwipeDismissableNavController()
    val context = LocalContext.current
    val mSettingsViewModel : SettingsViewModel = viewModel(
        factory = SettingsViewModelFactory(context.applicationContext as Application)
    )
    val itemsList = mSettingsViewModel.readAllData.observeAsState(listOf()).value
    SwipeDismissableNavHost(
        navController = swipeDismissibleNavController,
        startDestination = AppScreens.SplashScreen.route,
        modifier = Modifier.background(MaterialTheme.colors.background)
    ) {
        composable(route =  AppScreens.SplashScreen.route) {
            SplashScreen(swipeDismissibleNavController, viewModel = mSettingsViewModel, settings = itemsList)
        }
        composable(route =  AppScreens.MainScreen.route) {
            MainScreen(swipeDismissibleNavController, viewModel = mSettingsViewModel)
        }
        composable(route =  AppScreens.SettingsScreen.route) {
            SettingsScreen(swipeDismissibleNavController, settings = itemsList)
        }
        composable(
            route = AppScreens.ChangeDurationScreen.route,
            arguments = listOf(navArgument("id") {
                type = NavType.StringType
            })
        ) {
            val id = it.arguments?.getString("id") ?: Utility.WORK
            ChangeDurationScreen(swipeDismissibleNavController, viewModel = mSettingsViewModel, id = id, duration = mSettingsViewModel.getDurationTime(id))
        }
    }
}