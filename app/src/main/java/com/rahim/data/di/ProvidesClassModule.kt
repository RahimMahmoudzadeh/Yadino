package com.rahim.data.di

import com.rahim.broadcast.NotificationManager
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