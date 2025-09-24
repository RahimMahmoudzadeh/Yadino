package com.rahim.di

import com.rahim.data.reminder.alarm.ControlAlarm
import com.rahim.data.reminder.alarm.ControlAlarmImplementation
import com.rahim.yadino.core.timeDate.repo.DateTimeRepository
import com.rahim.yadino.core.timeDate.repo.DateTimeRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BindsModule {
  @Binds
  @Singleton
  abstract fun provideControlAlarm(controlAlarmImplementation: ControlAlarmImplementation): ControlAlarm

}
