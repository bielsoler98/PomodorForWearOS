package com.example.pomodoro.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SettingsDatabaseDao {

    @Query("SELECT * from settings")
    fun getAll(): LiveData<List<SettingsItem>>

    @Query("SELECT * from settings where item_name = :name")
    fun getById(name: String) : LiveData<SettingsItem?>

    @Insert
    fun insert(item: SettingsItem)

    @Update
    fun update(item:SettingsItem)

    @Delete
    fun delete(item: SettingsItem)

    @Query("DELETE FROM settings")
    fun deleteAllSettings()

}