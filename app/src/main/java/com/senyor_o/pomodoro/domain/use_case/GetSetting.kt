package com.senyor_o.pomodoro.domain.use_case

import com.senyor_o.pomodoro.domain.model.SettingsItem
import com.senyor_o.pomodoro.domain.repository.SettingsRepository
import javax.inject.Inject

class GetSetting @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(name: String): SettingsItem {
        return repository.getById(name)
    }
}