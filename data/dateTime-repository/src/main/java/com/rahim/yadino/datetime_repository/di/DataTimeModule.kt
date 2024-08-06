package com.rahim.yadino.datetime_repository.di

import com.rahim.yadino.dateTime.DataTimeRepository
import com.rahim.yadino.datetime_repository.DataTimeRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class DataTimeModule {
    @Binds
    abstract fun provideDataTimeRepository(dataTimeRepositoryImpl: DataTimeRepositoryImpl): DataTimeRepository
}