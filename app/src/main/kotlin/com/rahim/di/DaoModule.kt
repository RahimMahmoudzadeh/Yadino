package com.rahim.di

import com.rahim.yadino.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DaoModule {
  @Singleton
  @Provides
  fun providesRoutineDao(appDatabase: AppDatabase) = appDatabase.routineDao()

  @Singleton
  @Provides
  fun providesNoteDao(appDatabase: AppDatabase) = appDatabase.noteDao()

  @Singleton
  @Provides
  fun providesTimeDao(appDatabase: AppDatabase) = appDatabase.timeDataDao()
}
