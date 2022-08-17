package com.senyor_o.pomodoro.presentation.main_screen

sealed class MainEvent {

    object StartButtonClicked: MainEvent()

    object StartTimer: MainEvent()

    object StopTimer: MainEvent()

    object ResumeTimer: MainEvent()

    object RestartTimer: MainEvent()

    object StartNextTimer: MainEvent()
}