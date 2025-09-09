package com.rahim.di

import com.rahim.home.domain.HomeRepository
import com.rahim.home.domain.dateTime.DateTimeRepository
import com.rahim.yadino.home.data.dateTime.DateTimeRepositoryImpl
import com.rahim.yadino.home.data.HomeRepositoryImpl
import com.rahim.yadino.note.data.NoteRepositoryImpl
import com.rahim.yadino.note.domain.NoteRepository
import com.rahim.yadino.sharedPreferences.repo.SharedPreferencesRepository
import com.rahim.yadino.sharedPreferences.repo.SharedPreferencesRepositoryImpl
import com.yadino.routine.data.RoutineRepositoryImpl
import com.yadino.routine.domain.RoutineRepository
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
  abstract fun provideRoutineRepo(routineRepositoryImpl: RoutineRepositoryImpl): RoutineRepository

  @Binds
  @Singleton
  abstract fun provideHomeRepo(homeRepositoryImpl: HomeRepositoryImpl): HomeRepository

  @Binds
  @Singleton
  abstract fun provideNoteRepo(noteRepositoryImpl: NoteRepositoryImpl): NoteRepository

  @Binds
  @Singleton
  abstract fun provideSharedRepo(sharedPreferencesRepositoryImpl: SharedPreferencesRepositoryImpl): SharedPreferencesRepository
}
