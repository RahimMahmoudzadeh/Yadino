package com.rahim.data.di

import android.app.Application
import com.rahim.data.notification.NotificationManager
import com.rahim.data.sharedPreferences.SharedPreferencesCustom
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

  @Provides
  fun provideSharedPreferencesCustom(application: Application): SharedPreferencesCustom {
    return SharedPreferencesCustom(application)
  }
}
