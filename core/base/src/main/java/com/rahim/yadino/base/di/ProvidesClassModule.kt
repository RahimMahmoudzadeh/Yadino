package com.rahim.yadino.base.di

import android.app.Application
import com.rahim.yadino.base.sharedPreferences.SharedPreferencesCustom
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ProvidesClassModule {
    @Provides
    fun provideSharedPreferencesCustom(application: Application): SharedPreferencesCustom {
        return SharedPreferencesCustom(application)
    }
}