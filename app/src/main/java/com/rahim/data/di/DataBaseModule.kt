package com.rahim.data.di

import android.app.Application
import androidx.room.Room
import com.rahim.data.db.database.AppDatabase
import com.rahim.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataBaseModule {
  @Singleton
  @Provides
  fun providesMainDatabase(application: Application): AppDatabase {
    return Room.databaseBuilder(
      application,
      AppDatabase::class.java,
      Constants.DATABASE_NAME,
    ).build()
  }

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
