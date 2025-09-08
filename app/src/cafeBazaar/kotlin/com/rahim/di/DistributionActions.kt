package com.rahim.di

import android.content.Context
import com.rahim.CafeBazaarDistributionActionsImpl
import com.rahim.data.distributionActions.AppDistributionActions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DistributionActions {
  @Provides
  @Singleton
  fun provideCafeBazaarDistributionActions(@ApplicationContext context: Context): AppDistributionActions = CafeBazaarDistributionActionsImpl(context)
}
