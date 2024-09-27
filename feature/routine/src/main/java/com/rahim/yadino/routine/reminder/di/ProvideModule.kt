package com.rahim.yadino.routine.reminder.di

import android.app.AlarmManager
import android.content.Context
import com.rahim.yadino.routine.ReminderScheduler
import com.rahim.yadino.routine.reminder.ReminderSchedulerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class ProvideModule {

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
