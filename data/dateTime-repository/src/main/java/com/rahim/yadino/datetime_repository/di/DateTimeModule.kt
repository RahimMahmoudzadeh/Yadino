package com.rahim.yadino.datetime_repository.di

import com.rahim.yadino.dateTime.DateTimeRepository
import com.rahim.yadino.datetime_repository.DateTimeRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class DateTimeModule {
    @Binds
    abstract fun provideDateTimeRepository(dateTimeRepositoryImpl: DateTimeRepositoryImpl): DateTimeRepository
}