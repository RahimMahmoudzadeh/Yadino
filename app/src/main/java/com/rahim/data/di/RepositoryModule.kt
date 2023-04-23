package com.rahim.data.di

import com.rahim.data.repository.addRoutine.RepositoryRoutine
import com.rahim.data.repository.addRoutine.RoutineRepositoryImpl
import com.rahim.data.repository.dataTime.DataTimeRepository
import com.rahim.data.repository.dataTime.DataTimeRepositoryImpl
import com.rahim.data.repository.home.HomeRepository
import com.rahim.data.repository.home.HomeRepositoryImpl
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
    abstract fun provideRepositoryTask(repositoryAddTaskImpl: RoutineRepositoryImpl):RepositoryRoutine

    @Binds
    abstract fun provideRepositoryDataTime(dataTimeRepositoryImpl: DataTimeRepositoryImpl):DataTimeRepository
    @Binds
    abstract fun provideSharedPreferencesRepository(sharedPreferencesRepositoryImpl: SharedPreferencesRepositoryImpl):SharedPreferencesRepository
    @Binds
    abstract fun provideHomeRepository(homeRepositoryImpl: HomeRepositoryImpl):HomeRepository
}