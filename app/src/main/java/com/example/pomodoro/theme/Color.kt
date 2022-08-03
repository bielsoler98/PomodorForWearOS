package com.example.pomodoro.theme

import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.Colors

val TomatoRed200 = Color(0xFFd32f2f)
val TomatoRed500 = Color(0xFFff6659)
val TomatoRed700 = Color(0xFF9a0007)
val Teal200 = Color(0xFF669e37)
val Red400 = Color(0xFFCF6679)

val WearAppColorPalette: Colors = Colors(
    primary = TomatoRed200,
    primaryVariant = TomatoRed700,
    secondary = Teal200,
    secondaryVariant = Teal200,
    error = Red400,
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onError = Color.Black
)
