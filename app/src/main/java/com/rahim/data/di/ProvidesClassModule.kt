package com.rahim.data.di

import com.rahim.data.notification.NotificationManager
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