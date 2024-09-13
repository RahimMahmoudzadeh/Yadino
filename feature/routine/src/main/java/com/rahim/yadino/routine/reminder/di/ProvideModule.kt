package com.rahim.yadino.routine.reminder.di

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
    fun provideReminderScheduler(@ApplicationContext context: Context): ReminderScheduler {
        return ReminderSchedulerImpl(context)
    }
}