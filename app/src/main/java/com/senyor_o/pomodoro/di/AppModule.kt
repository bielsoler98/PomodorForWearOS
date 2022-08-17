package com.senyor_o.pomodoro.di

import android.app.Application
import androidx.room.Room
import com.senyor_o.pomodoro.data.repository.SettingsRepositoryImpl
import com.senyor_o.pomodoro.data.room.SettingsDatabase
import com.senyor_o.pomodoro.domain.repository.SettingsRepository
import com.senyor_o.pomodoro.utils.Utility.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideSettingsDatabase(app: Application) = Room.databaseBuilder(
        app,
        SettingsDatabase::class.java,
        DATABASE_NAME
    ).fallbackToDestructiveMigration()
        .createFromAsset("database/settings.db").build()

    @Provides
    @Singleton
    fun provideRepository(db: SettingsDatabase): SettingsRepository =
        SettingsRepositoryImpl(db.settingsDao)

}