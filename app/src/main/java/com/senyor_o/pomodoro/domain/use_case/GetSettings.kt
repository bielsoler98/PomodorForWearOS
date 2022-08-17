package com.senyor_o.pomodoro.domain.use_case

import com.senyor_o.pomodoro.domain.model.SettingsItem
import com.senyor_o.pomodoro.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSettings @Inject constructor(
    private val repository: SettingsRepository
) {
    operator fun invoke(): Flow<List<SettingsItem>> {
        return repository.getAll()
    }
}