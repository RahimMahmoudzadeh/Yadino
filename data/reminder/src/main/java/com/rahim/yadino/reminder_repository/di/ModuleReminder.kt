package com.rahim.yadino.reminder_repository.di

import android.content.Context
import com.rahim.yadino.reminder.ReminderScheduler
import com.rahim.yadino.reminder_repository.AndroidReminderScheduler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ModuleReminder {
    @Provides
    @Singleton
    fun provideReminderScheduler(@ApplicationContext context: Context): ReminderScheduler {
        return AndroidReminderScheduler(context)
    }
}