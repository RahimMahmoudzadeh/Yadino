package com.yadino.routine.presentation.di

import com.yadino.routine.presentation.RoutineScreenViewModel
import com.yadino.routine.presentation.history.HistoryViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val RoutineUiDiModule = module {
  viewModel {
    RoutineScreenViewModel(
      addReminderUseCase = get(),
      cancelReminderUseCase = get(),
      deleteReminderUseCase = get(),
      getRemindersUseCase = get(),
      searchRoutineUseCase = get(),
      updateReminderUseCase = get(),
      dateTimeRepository = get(),
      ioDispatcher = get(),
    )
  }
  viewModel { HistoryViewModel(getRoutineUseCase = get()) }
}
