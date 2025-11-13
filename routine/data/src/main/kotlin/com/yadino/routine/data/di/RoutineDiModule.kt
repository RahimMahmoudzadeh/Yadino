package com.yadino.routine.data.di

import com.yadino.routine.data.repoImpl.RoutineRepositoryImpl
import com.rahim.yadino.routine.domain.repo.RoutineRepository
import com.rahim.yadino.routine.domain.useCase.AddReminderUseCase
import com.rahim.yadino.routine.domain.useCase.CancelReminderUseCase
import com.rahim.yadino.routine.domain.useCase.DeleteReminderUseCase
import com.rahim.yadino.routine.domain.useCase.GetAllRoutineUseCase
import com.rahim.yadino.routine.domain.useCase.GetCurrentTimeUseCase
import com.rahim.yadino.routine.domain.useCase.GetRemindersUseCase
import com.rahim.yadino.routine.domain.useCase.GetTimesMonthUseCase
import com.rahim.yadino.routine.domain.useCase.SearchRoutineUseCase
import com.rahim.yadino.routine.domain.useCase.UpdateReminderUseCase
import org.koin.dsl.module

val RoutineDiModule = module {
  single<RoutineRepository> { RoutineRepositoryImpl(routineDao = get(), sharedPreferencesRepository = get()) }
  single { AddReminderUseCase(routineRepository = get(), reminderScheduler = get()) }
  single { CancelReminderUseCase(routineRepository = get(), reminderScheduler = get()) }
  single { DeleteReminderUseCase(routineRepository = get(), reminderScheduler = get()) }
  single { GetAllRoutineUseCase(routineRepository = get()) }
  single { GetRemindersUseCase(routineRepository = get()) }
  single { SearchRoutineUseCase(routineRepository = get()) }
  single { UpdateReminderUseCase(routineRepository = get(), reminderScheduler = get()) }
  single { GetTimesMonthUseCase(dateTimeRepository = get()) }
  single { GetCurrentTimeUseCase(dateTimeRepository = get()) }
}
