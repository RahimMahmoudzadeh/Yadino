package com.rahim.yadino.home.presentation.di

import com.rahim.yadino.home.presentation.HomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val HomeDiPresentationModule = module {
  viewModel {
    HomeViewModel(
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
