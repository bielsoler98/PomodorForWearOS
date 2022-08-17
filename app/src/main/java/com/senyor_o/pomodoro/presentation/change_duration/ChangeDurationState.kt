package com.senyor_o.pomodoro.presentation.change_duration

import com.senyor_o.pomodoro.domain.model.SettingsItem

data class ChangeDurationState(
    val settingName: String = "",
    val settingDuration: Long = 0L
)
