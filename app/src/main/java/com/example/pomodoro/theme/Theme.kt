package com.example.pomodoro.theme

import androidx.compose.runtime.Composable
import androidx.wear.compose.material.MaterialTheme
import com.example.pomodoro.theme.WearAppColorPalette
import com.example.pomodoro.theme.WearAppTypography

@Composable
fun WearAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = WearAppColorPalette,
        typography = WearAppTypography,
        // For shapes, we generally recommend using the default Material Wear shapes which are
        // optimized for round and non-round devices.
        content = content
    )
}