package com.yadino.routine.presentation.di

import com.rahim.yadino.enums.DispatchersQualifier
import com.yadino.routine.presentation.navigation.RoutineComponentImpl
import com.yadino.routine.presentation.navigation.history.HistoryRoutineComponentImpl
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val RoutineUiDiModule = module {
  viewModel {
    RoutineComponentImpl(
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
  viewModel { HistoryRoutineComponentImpl(getAllRoutineUseCase = get()) }
}
