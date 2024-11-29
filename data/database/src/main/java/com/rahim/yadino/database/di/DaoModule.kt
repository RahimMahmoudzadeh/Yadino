package com.rahim.yadino.database.di

import com.rahim.yadino.database.AppDatabase
import dagger.hilt.components.SingletonComponent

@dagger.hilt.InstallIn(SingletonComponent::class)
@dagger.Module
object DaoModule {
  @javax.inject.Singleton
  @dagger.Provides
  fun providesRoutineDao(appDatabase: AppDatabase) =
    appDatabase.routineDao()

  @javax.inject.Singleton
  @dagger.Provides
  fun providesNoteDao(appDatabase: AppDatabase) = appDatabase.noteDao()

  @javax.inject.Singleton
  @dagger.Provides
  fun providesTimeDao(appDatabase: AppDatabase) =
    appDatabase.timeDataDao()
}
