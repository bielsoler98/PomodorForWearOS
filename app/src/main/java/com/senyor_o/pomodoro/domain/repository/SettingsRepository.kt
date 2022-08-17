package com.senyor_o.pomodoro.domain.repository

import com.senyor_o.pomodoro.domain.model.SettingsItem
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    fun getAll(): Flow<List<SettingsItem>>

    suspend fun getById(name: String) : SettingsItem

    suspend fun insert(item: SettingsItem)

    suspend fun delete(item: SettingsItem)


}