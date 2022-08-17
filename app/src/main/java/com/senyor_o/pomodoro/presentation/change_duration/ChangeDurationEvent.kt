package com.senyor_o.pomodoro.presentation.change_duration

sealed class ChangeDurationEvent{
    object AddTime : ChangeDurationEvent()

    object ReduceTime : ChangeDurationEvent()

    object SaveTime : ChangeDurationEvent()
}
