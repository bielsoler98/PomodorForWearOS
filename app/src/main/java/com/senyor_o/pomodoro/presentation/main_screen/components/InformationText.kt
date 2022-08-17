package com.senyor_o.pomodoro.presentation.main_screen.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import com.senyor_o.pomodoro.domain.model.PomodoroState

@Composable
fun InformationText(
    currentTime: String,
    currentState: PomodoroState,
    modifier: Modifier
) {
    Text(
        text = currentState.label,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier.padding(top = 64.dp)
    )
    Text(
        text = currentTime,
        fontSize = 44.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier
    )
}