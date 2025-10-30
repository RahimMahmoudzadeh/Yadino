package com.rahim.yadino.home.presentation.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val HomeDiPresentationModule = module {
  viewModel {
    HomeComponent(
      addReminderUseCase = get(),
      updateReminderUseCase = get(),
      cancelReminderUseCase = get(),
      deleteReminderUseCase = get(),
      getTodayRoutinesUseCase = get(),
      searchRoutineUseCase = get(),
      getCurrentDateUseCase = get(),
    )
  }
}
