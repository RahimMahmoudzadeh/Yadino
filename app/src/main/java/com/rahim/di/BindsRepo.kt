package com.rahim.di

import com.example.sharedPreferences.SharedPreferencesRepositoryImpl
import com.rahim.yadino.dateTime.DateTimeRepository
import com.rahim.yadino.datetime_repository.DateTimeRepositoryImpl
import com.rahim.yadino.note.NoteRepository
import com.rahim.yadino.note_repository.NoteRepositoryImpl
import com.rahim.yadino.routine.RepositoryRoutine
import com.rahim.yadino.routine_repository.RoutineRepositoryImpl
import com.rahim.yadino.sharedPreferences.SharedPreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BindsRepo {
  @Binds
  @Singleton
  abstract fun provideDateTimeRepo(dateTimeRepositoryImpl: DateTimeRepositoryImpl): DateTimeRepository

  @Binds
  @Singleton
  abstract fun provideRoutineRepo(routineRepositoryImpl: RoutineRepositoryImpl): RepositoryRoutine

  @Binds
  @Singleton
  abstract fun provideNoteRepo(noteRepositoryImpl: NoteRepositoryImpl): NoteRepository

  @Binds
  @Singleton
  abstract fun provideSharedRepo(sharedPreferencesRepositoryImpl: SharedPreferencesRepositoryImpl): SharedPreferencesRepository

}
