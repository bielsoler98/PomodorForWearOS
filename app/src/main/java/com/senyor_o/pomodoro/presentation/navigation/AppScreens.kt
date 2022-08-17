package com.senyor_o.pomodoro.presentation.navigation

sealed class AppScreens(val route: String) {
    object MainScreen: AppScreens("main_screen")
    object SettingsScreen: AppScreens("settings_screen")
    object ChangeDurationScreen: AppScreens(route = "duration_screen/{id}") {
        fun passId(id: String): String{
            return "duration_screen/$id"
        }
    }
}
