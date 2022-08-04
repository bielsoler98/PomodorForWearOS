package com.senyor_o.pomodoro.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="settings")
class SettingsItem (
    @PrimaryKey
    @ColumnInfo(name="item_name")
    val itemName: String,

    @ColumnInfo(name="time_duration")
    var duration: Long = 0L
    )