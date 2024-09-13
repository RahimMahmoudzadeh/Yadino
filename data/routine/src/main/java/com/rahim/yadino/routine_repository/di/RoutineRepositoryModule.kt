package com.rahim.yadino.routine_repository.di

import com.rahim.yadino.routine.RepositoryRoutine
import com.rahim.yadino.routine_repository.RoutineRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RoutineRepositoryModule {
    @Binds
    @Singleton
    internal abstract fun provideRoutineRepository(routineRepositoryImpl: RoutineRepositoryImpl): RepositoryRoutine
}