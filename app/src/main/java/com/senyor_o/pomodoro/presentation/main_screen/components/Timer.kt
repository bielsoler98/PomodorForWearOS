package com.senyor_o.pomodoro.presentation.main_screen.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.senyor_o.pomodoro.domain.model.PomodoroState
import com.senyor_o.pomodoro.theme.WearAppColorPalette
import com.senyor_o.pomodoro.utils.Utility.formatTime
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun Timer(
    progress: Float,
    currentTime: Long,
    pomodoroState: PomodoroState,
    size: IntSize,
    modifier: Modifier = Modifier,
    startButton: @Composable () -> Unit = {},
    endButton: @Composable () -> Unit = {},
    handColor: Color = WearAppColorPalette.secondary,
    inactiveBarColor: Color = Color.DarkGray,
    activeBarColor: Color = WearAppColorPalette.secondary,
    strokeWidth: Dp = 5.dp
) {
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
                sweepAngle = -250f * progress,
                useCenter = false,
                size = Size(size.width.toFloat(), size.height.toFloat()),
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )
            val center = Offset(size.width / 2f, size.height / 2f)
            val beta = (250f * (-progress) + 215) * (Math.PI / 180f).toFloat()
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
            startButton()
            Spacer(modifier = Modifier.padding(8.dp))
            endButton()
        }
    }
}