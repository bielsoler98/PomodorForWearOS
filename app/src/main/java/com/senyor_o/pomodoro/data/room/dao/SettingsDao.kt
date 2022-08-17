package com.senyor_o.pomodoro.data.room.dao

import androidx.room.*
import com.senyor_o.pomodoro.domain.model.SettingsItem
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao {

    @Query("SELECT * from settings ORDER BY item_name DESC")
    fun getAll(): Flow<List<SettingsItem>>

    @Query("SELECT * from settings where item_name = :name" )
    suspend fun getById(name: String) : SettingsItem

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: SettingsItem)

    @Delete
    suspend fun delete(item: SettingsItem)

}