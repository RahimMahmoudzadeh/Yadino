package com.rahim.di

import android.app.AlarmManager
import android.content.Context
import com.rahim.data.reminder.ReminderSchedulerImpl
import com.rahim.yadino.base.reminder.ReminderScheduler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Provide{
  @Provides
  @Singleton
  fun provideAlarmManager(@ApplicationContext context: Context): AlarmManager {
    return context.getSystemService(AlarmManager::class.java)
  }
  @Provides
  @Singleton
  fun provideReminderScheduler(alarmManager: AlarmManager, @ApplicationContext context: Context): ReminderScheduler {
    return ReminderSchedulerImpl(alarmManager, context)
  }
}
