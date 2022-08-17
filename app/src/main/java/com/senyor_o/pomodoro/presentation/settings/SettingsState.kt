package com.senyor_o.pomodoro.presentation.settings

import com.senyor_o.pomodoro.domain.model.SettingsItem

data class SettingsState(
    val settings: List<SettingsItem> = emptyList(),
)
