package com.senyor_o.pomodoro.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.wear.compose.material.*
import com.senyor_o.pomodoro.*
import com.senyor_o.pomodoro.domain.model.hasEnded
import com.senyor_o.pomodoro.presentation.main_screen.MainEvent
import com.senyor_o.pomodoro.presentation.main_screen.MainViewModel
import com.senyor_o.pomodoro.presentation.main_screen.components.Timer
import com.senyor_o.pomodoro.presentation.navigation.AppScreens
import com.senyor_o.pomodoro.theme.WearAppColorPalette

@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    Scaffold(
        timeText = {
            TimeText()
        }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .onSizeChanged {
                    size = IntSize(it.width, it.width)
                }
        ) {
            Timer(
                progress = state.progress,
                currentTime = state.millisUntilFinished,
                pomodoroState = state.pomodoroState,
                size = size,
                startButton = {
                    Button(
                        onClick = {
                            viewModel.handleCountDownTimer()
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = if (state.pomodoroState.hasEnded() || !state.isPlaying || state.millisUntilFinished <= 0L) {
                                WearAppColorPalette.secondary
                            } else {
                                WearAppColorPalette.primary
                            }
                        )
                    ) {
                        Icon(
                            imageVector = if (state.isPlaying  && state.millisUntilFinished > 0L) {
                                Icons.Rounded.Pause
                            } else if(!state.pomodoroState.hasEnded()) {
                                Icons.Rounded.PlayArrow
                            } else {
                                Icons.Rounded.RestartAlt
                            },
                            contentDescription = "triggers timer action",
                            modifier = Modifier
                                .size(24.dp)
                                .wrapContentSize(align = Alignment.Center)
                        )
                    }
                },
                endButton = {
                    Button(
                        onClick = {
                            if (!state.isPlaying && (state.millisUntilFinished == 0L || state.millisUntilFinished == state.currentDuration)) {
                                navController.navigate(AppScreens.SettingsScreen.route)
                            } else {
                                viewModel.onEvent(MainEvent.RestartTimer)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = if (!state.isPlaying && (state.millisUntilFinished == 0L || state.millisUntilFinished == state.currentDuration)) {
                                Color.DarkGray
                            } else {
                                WearAppColorPalette.primary
                            }
                        )
                    ) {
                        Icon(
                            imageVector = if (!state.isPlaying && (state.millisUntilFinished == 0L || state.millisUntilFinished == state.currentDuration)) {
                                Icons.Rounded.Settings
                            } else {
                                Icons.Rounded.Stop
                            },
                            contentDescription = "triggers timer action",
                            modifier = Modifier
                                .size(24.dp)
                                .wrapContentSize(align = Alignment.Center)
                        )
                    }
                }
            )
        }
    }
}

@Preview(
    widthDp = WEAR_PREVIEW_DEVICE_WIDTH_DP,
    heightDp = WEAR_PREVIEW_DEVICE_HEIGHT_DP,
    apiLevel = WEAR_PREVIEW_API_LEVEL,
    uiMode = WEAR_PREVIEW_UI_MODE,
    backgroundColor = WEAR_PREVIEW_BACKGROUND_COLOR_BLACK,
    showBackground = WEAR_PREVIEW_SHOW_BACKGROUND,
)
@Preview(
    widthDp = WEAR_PREVIEW_DEVICE_WIDTH_DP,
    heightDp = WEAR_PREVIEW_DEVICE_HEIGHT_DP,
    apiLevel = WEAR_PREVIEW_API_LEVEL,
    uiMode = WEAR_PREVIEW_UI_MODE,
    backgroundColor = WEAR_PREVIEW_BACKGROUND_COLOR_BLACK,
    showBackground = WEAR_PREVIEW_SHOW_BACKGROUND,
    device = Devices.WEAR_OS_LARGE_ROUND,
    name = "Round"
)
@Composable
fun MainScreenPreview() {
    val navController = NavController(LocalContext.current)
    MainScreen(navController)
}