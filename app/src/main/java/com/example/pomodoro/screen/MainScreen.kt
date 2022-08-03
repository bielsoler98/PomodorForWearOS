package com.example.pomodoro.screen

import android.app.Application
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.wear.compose.material.*
import com.example.pomodoro.*
import com.example.pomodoro.database.PomodoroState
import com.example.pomodoro.database.SettingsItem
import com.example.pomodoro.database.SettingsViewModel
import com.example.pomodoro.database.SettingsViewModelFactory
import com.example.pomodoro.navigation.AppScreens
import com.example.pomodoro.theme.WearAppColorPalette
import com.example.pomodoro.theme.WearAppTheme
import com.example.pomodoro.utils.Utility
import com.example.pomodoro.utils.Utility.formatTime
import java.lang.Math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: SettingsViewModel
) {
    WearAppTheme {
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
                    viewModel = viewModel,
                    navController = navController,
                    size = size
                )
            }
        }
    }
}

@Composable
fun Timer(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = viewModel(),
    size: IntSize,
    handColor: Color = WearAppColorPalette.secondary,
    inactiveBarColor: Color = Color.DarkGray,
    activeBarColor: Color = WearAppColorPalette.secondary,
    navController: NavHostController,
    strokeWidth: Dp = 5.dp,
) {
    val value by viewModel.progress.observeAsState(1.00F)
    val isTimerRunning by viewModel.isPlaying.observeAsState(false)
    val pomodoroState by viewModel.pomodoroState.observeAsState(PomodoroState.WORK_1)
    val totalTime by remember {
        mutableStateOf(
            viewModel.getTotalTime().also {
                viewModel.initMillisUntilFinished()
            }
        )
    }
    val currentTime by viewModel.millisUntilFinished.observeAsState(totalTime)
    Box(
        modifier = Modifier
            .then(
                with(LocalDensity.current) {
                    Modifier.size(
                        width = size.width.toDp(),
                        height = size.width.toDp(),
                    )
                }
            )

    ) {
        Canvas(
            modifier = modifier
        ) {
            drawArc(
                color = inactiveBarColor,
                startAngle = 215f,
                sweepAngle = -250f,
                useCenter = false,
                size = Size(size.width.toFloat(), size.height.toFloat()),
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )
            drawArc(
                color = activeBarColor,
                startAngle = 215f,
                sweepAngle = -250f * value,
                useCenter = false,
                size = Size(size.width.toFloat(), size.height.toFloat()),
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )
            val center = Offset(size.width / 2f, size.height / 2f)
            val beta = (250f * (-value) + 215) * (PI / 180f).toFloat()
            val r = size.width / 2f
            val a = cos(beta) * r
            val b = sin(beta) * r
            drawPoints(
                listOf(Offset(center.x + a, center.y + b)),
                pointMode = PointMode.Points,
                color = handColor,
                strokeWidth = (strokeWidth * 3f).toPx(),
                cap = StrokeCap.Round
            )
        }
        InformationText(
            currentTime.formatTime(),
            pomodoroState,
            modifier = Modifier
                .align(Alignment.Center)
        )
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp)
        ) {
            if(pomodoroState != PomodoroState.END) {
                Button(
                    onClick = {
                        viewModel.handleCountDownTimer()
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (!isTimerRunning || currentTime <= 0L) {
                            WearAppColorPalette.secondary
                        } else {
                            WearAppColorPalette.primary
                        }
                    )
                ) {
                    Icon(
                        imageVector = if (isTimerRunning && currentTime > 0L) {
                            Icons.Rounded.Pause
                        } else {
                            Icons.Rounded.PlayArrow
                        },
                        contentDescription = "triggers timer action",
                        modifier = Modifier
                            .size(24.dp)
                            .wrapContentSize(align = Alignment.Center)
                    )
                }
            } else {
                Button(
                    onClick = {
                        viewModel.setPomodoroState(PomodoroState.WORK_1)
                        viewModel.handleCountDownTimer()
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = WearAppColorPalette.secondary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.RestartAlt,
                        contentDescription = "triggers timer action",
                        modifier = Modifier
                            .size(24.dp)
                            .wrapContentSize(align = Alignment.Center)
                    )
                }
            }
            Spacer(modifier = Modifier.padding(8.dp))
            if (!isTimerRunning && (currentTime == 0L || currentTime == totalTime)) {
                Button(
                    onClick = {
                        navController.navigate(AppScreens.SettingsScreen.route)
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.DarkGray
                    )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Settings,
                        contentDescription = "Settings",
                        modifier = Modifier
                            .size(24.dp)
                            .wrapContentSize(align = Alignment.Center)
                    )
                }
            } else {
                Button(
                    onClick = {
                        viewModel.setPomodoroState(PomodoroState.WORK_1)
                        viewModel.cancelTimer()
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = WearAppColorPalette.primary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Stop,
                        contentDescription = "Stop Pomodoro",
                        modifier = Modifier
                            .size(24.dp)
                            .wrapContentSize(align = Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
private fun InformationText(
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
    val context = LocalContext.current
    val mSettingsViewModel : SettingsViewModel = viewModel(
        factory = SettingsViewModelFactory(context.applicationContext as Application)
    )
    val navController = NavHostController(context)
    MainScreen(navController, mSettingsViewModel)
}