package com.rahim.di

import android.app.AlarmManager
import android.content.Context
import com.rahim.data.reminder.NotificationManager
import com.rahim.data.reminder.ReminderSchedulerImpl
import com.rahim.data.reminder.alarm.ControlAlarm
import com.rahim.yadino.base.reminder.ReminderScheduler
import com.rahim.yadino.db.note.dao.NoteDao
import com.rahim.yadino.note.data.NoteRepositoryImpl
import com.rahim.yadino.note.domain.NoteRepository
import com.rahim.yadino.sharedPreferences.repo.SharedPreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProvideModule {
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

  @Provides
  @Singleton
  fun provideNoteRepo(noteDao: NoteDao, sharedPreferencesRepository: SharedPreferencesRepository): NoteRepository {
    return NoteRepositoryImpl(noteDao = noteDao, sharedPreferencesRepository = sharedPreferencesRepository)
  }

  @Provides
  @Singleton
  fun provideNotificationManager(controlAlarm: ControlAlarm): NotificationManager {
    return NotificationManager(controlAlarm = controlAlarm)
  }
}
