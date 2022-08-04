package com.senyor_o.pomodoro.database

import androidx.lifecycle.LiveData

class SettingsRepository (private val settingsDatabaseDao: SettingsDatabaseDao) {

    val readAllData : LiveData<List<SettingsItem>> =  settingsDatabaseDao.getAll()

    fun updateSetting(settingsItem: SettingsItem) {
        settingsDatabaseDao.update(settingsItem)
    }

}