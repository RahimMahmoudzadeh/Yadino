package com.rahim.data.di

import com.rahim.data.repository.base.BaseRepository
import com.rahim.data.repository.base.BaseRepositoryImpl
import com.rahim.data.repository.routine.RepositoryRoutine
import com.rahim.data.repository.routine.RoutineRepositoryImpl
import com.rahim.data.repository.dataTime.DataTimeRepository
import com.rahim.data.repository.dataTime.DataTimeRepositoryImpl
import com.rahim.data.repository.home.HomeRepository
import com.rahim.data.repository.home.HomeRepositoryImpl
import com.rahim.data.repository.note.NoteRepository
import com.rahim.data.repository.note.NoteRepositoryImpl
import com.rahim.data.repository.sharedPreferences.SharedPreferencesRepository
import com.rahim.data.repository.sharedPreferences.SharedPreferencesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    abstract fun provideRepositoryTask(repositoryAddTaskImpl: RoutineRepositoryImpl): RepositoryRoutine

    @Binds
    abstract fun provideRepositoryDataTime(dataTimeRepositoryImpl: DataTimeRepositoryImpl): DataTimeRepository

    @Binds
    abstract fun provideSharedPreferencesRepository(sharedPreferencesRepositoryImpl: SharedPreferencesRepositoryImpl): SharedPreferencesRepository

    @Binds
    abstract fun provideHomeRepository(homeRepositoryImpl: HomeRepositoryImpl): HomeRepository

    @Binds
    abstract fun provideNoteRepository(noteRepositoryImpl: NoteRepositoryImpl): NoteRepository

    @Binds
    abstract fun provideBaseRepository(baseRepositoryImpl: BaseRepositoryImpl): BaseRepository
}