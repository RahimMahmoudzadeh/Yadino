package com.yadino.routine.presentation.di

import android.app.AlarmManager
import android.content.Context
import com.yadino.routine.presentation.ReminderSchedulerImpl
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
  fun provideReminderScheduler(alarmManager: AlarmManager, @ApplicationContext context: Context): ReminderScheduler {
    return ReminderSchedulerImpl(alarmManager, context)
  }
}
