package com.senyor_o.pomodoro.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.senyor_o.pomodoro.data.room.dao.SettingsDao
import com.senyor_o.pomodoro.domain.model.SettingsItem

@Database(
    entities = [SettingsItem::class],
    version = 1,
    exportSchema = false
)
abstract class SettingsDatabase: RoomDatabase() {
    abstract val settingsDao: SettingsDao
}