package com.rahim.data.di

import android.app.Application
import com.rahim.yadino.wekeup.notification.NotificationManager
import com.rahim.yadino.base.sharedPreferences.SharedPreferencesCustom
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class ProvidesClassModule {
    @Provides
    fun provideNotificationManager(): NotificationManager {
        return NotificationManager()
    }
}