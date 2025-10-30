package com.yadino.routine.presentation.di

import com.rahim.yadino.enums.DispatchersQualifier
import com.yadino.routine.presentation.RoutineScreenComponent
import com.yadino.routine.presentation.history.HistoryComponent
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val RoutineUiDiModule = module {
  viewModel {
    RoutineScreenComponent(
      addReminderUseCase = get(),
      cancelReminderUseCase = get(),
      deleteReminderUseCase = get(),
      getRemindersUseCase = get(),
      searchRoutineUseCase = get(),
      updateReminderUseCase = get(),
      dateTimeRepository = get(),
      ioDispatcher = get((named(DispatchersQualifier.IO))),
    )
  }
  viewModel { HistoryComponent(getRoutineUseCase = get()) }
}
