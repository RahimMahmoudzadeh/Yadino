package com.rahim.di

import android.content.Context
import com.rahim.yadino.sharedPreferences.repo.SharedPreferencesRepositoryImpl
import com.rahim.FlavorImpl
import com.rahim.data.flavor.Flavor
import com.rahim.yadino.dateTime.DateTimeRepository
import com.rahim.yadino.datetimeRepository.DateTimeRepositoryImpl
import com.rahim.yadino.note.NoteRepository
import com.rahim.yadino.noteRepository.NoteRepositoryImpl
import com.rahim.yadino.routine.RepositoryRoutine
import com.rahim.yadino.routineRepository.RoutineRepositoryImpl
import com.rahim.yadino.sharedPreferences.repo.SharedPreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

@Module
@InstallIn(SingletonComponent::class)
object ProvideRepo {
  @Provides
  @Singleton
  fun provideFlavor(@ApplicationContext context: Context): Flavor = FlavorImpl(context)
}
