package com.example.sharedPreferences.di

import android.content.SharedPreferences
import com.example.sharedPreferences.SharedPreferencesRepositoryImpl
import com.rahim.yadino.sharedPreferences.SharedPreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SharedPreferencesModule {
    @Binds
    @Singleton
    abstract fun provideSharedPreferences(sharedPreferencesImpl: SharedPreferencesRepositoryImpl): SharedPreferencesRepository
}