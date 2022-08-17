package com.senyor_o.pomodoro.domain.use_case

import com.senyor_o.pomodoro.domain.model.SettingsItem
import com.senyor_o.pomodoro.domain.repository.SettingsRepository
import javax.inject.Inject

class InsertSetting @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(setting: SettingsItem) {
        repository.insert(setting)
    }
}