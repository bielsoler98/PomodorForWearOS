package com.senyor_o.pomodoro.data.repository

import com.senyor_o.pomodoro.data.room.dao.SettingsDao
import com.senyor_o.pomodoro.domain.model.SettingsItem
import com.senyor_o.pomodoro.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val dao: SettingsDao,
): SettingsRepository {
    override fun getAll(): Flow<List<SettingsItem>> {
        return dao.getAll()
    }

    override suspend fun getById(name: String): SettingsItem {
        return dao.getById(name)
    }

    override suspend fun insert(item: SettingsItem) {
        dao.insert(item)
    }

    override suspend fun delete(item: SettingsItem) {
        dao.delete(item)
    }

}