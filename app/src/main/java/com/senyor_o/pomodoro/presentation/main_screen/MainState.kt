package com.senyor_o.pomodoro.presentation.main_screen

import com.senyor_o.pomodoro.domain.model.PomodoroState
import com.senyor_o.pomodoro.domain.model.SettingsItem
import com.senyor_o.pomodoro.utils.CountDownTimer

data class MainState(
    val settings: List<SettingsItem> = emptyList(),
    val pomodoroState: PomodoroState = PomodoroState.WORK_1,
    val progress: Float = 1.00f,
    val isPlaying: Boolean = false,
    val currentDuration: Long = 0L,
    val millisUntilFinished: Long = 0L,
    var countDownTimer: CountDownTimer? = null
)
