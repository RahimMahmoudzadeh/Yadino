package com.rahim.yadino.di

import com.rahim.yadino.enums.DispatchersQualifier
import com.rahim.yadino.sharedPreferences.SharedPreferencesCustom
import com.rahim.yadino.sharedPreferences.repo.SharedPreferencesRepository
import com.rahim.yadino.sharedPreferences.repo.SharedPreferencesRepositoryImpl
import com.rahim.yadino.sharedPreferences.useCase.ChangeThemeUseCase
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val CoreDiModule = module {
  single { SharedPreferencesCustom(context = androidContext()) }
  single<SharedPreferencesRepository> { SharedPreferencesRepositoryImpl(sharedPreferencesCustom = get()) }
  single { ChangeThemeUseCase(sharedPreferencesRepository = get()) }
  single(named(DispatchersQualifier.Default)) { Dispatchers.Default }
  single(named(DispatchersQualifier.IO)) { Dispatchers.IO }
  single(named(DispatchersQualifier.Main)) { Dispatchers.Main }
}
