package com.rahim.yadino.home.data.di

import com.rahim.home.domain.repo.HomeRepository
import com.rahim.yadino.home.data.repoImpl.HomeRepositoryImpl
import org.koin.dsl.module

val homeDiModule = module {
  single<HomeRepository> { HomeRepositoryImpl(routineDao = get(), sharedPreferencesRepository = get()) }
}
