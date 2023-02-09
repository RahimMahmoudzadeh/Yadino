package com.rahim.data.di

import com.rahim.data.repository.addRoutine.RepositoryRoutine
import com.rahim.data.repository.addRoutine.RepositoryRoutineImpl
import com.rahim.data.repository.dataTime.DataTimeRepository
import com.rahim.data.repository.dataTime.DataTimeRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    abstract fun provideRepositoryTask(repositoryAddTaskImpl: RepositoryRoutineImpl):RepositoryRoutine

    @Binds
    abstract fun provideRepositoryDataTime(dataTimeRepositoryImpl: DataTimeRepositoryImpl):DataTimeRepository
}