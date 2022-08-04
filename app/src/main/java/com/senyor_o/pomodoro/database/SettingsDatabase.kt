package com.senyor_o.pomodoro.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [SettingsItem::class], version = 1)
abstract class SettingsDatabase: RoomDatabase() {
    abstract fun settingsDao(): SettingsDatabaseDao

    companion object {
        private var INSTANCE: SettingsDatabase? = null
        fun getInstance(context: Context): SettingsDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SettingsDatabase::class.java,
                        "settings_database"
                    ).fallbackToDestructiveMigration()
                        .createFromAsset("database/settings.db")
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}