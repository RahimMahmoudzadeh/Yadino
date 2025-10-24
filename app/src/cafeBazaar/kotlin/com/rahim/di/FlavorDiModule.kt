package com.rahim.di

import com.rahim.CafeBazaarDistributionActionsImpl
import com.rahim.data.distributionActions.AppDistributionActions
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val FlavorDiModule= module {
  single<AppDistributionActions> { CafeBazaarDistributionActionsImpl(context = androidContext()) }
}
