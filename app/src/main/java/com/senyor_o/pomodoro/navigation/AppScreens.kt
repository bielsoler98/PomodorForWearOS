package com.senyor_o.pomodoro.navigation

sealed class AppScreens(val route: String) {
    object SplashScreen: AppScreens("splash_screen")
    object MainScreen: AppScreens("main_screen")
    object SettingsScreen: AppScreens("settings_screen")
    object ChangeDurationScreen: AppScreens(route = "duration_screen/{id}")
}
