package com.rahim.yadino.home.data.di

import com.rahim.home.domain.repo.HomeRepository
import com.rahim.home.domain.useCase.AddReminderUseCase
import com.rahim.home.domain.useCase.CancelReminderUseCase
import com.rahim.home.domain.useCase.DeleteReminderUseCase
import com.rahim.home.domain.useCase.GetCurrentDateUseCase
import com.rahim.home.domain.useCase.GetTodayRoutinesUseCase
import com.rahim.home.domain.useCase.SearchRoutineUseCase
import com.rahim.home.domain.useCase.UpdateReminderUseCase
import com.rahim.yadino.home.data.repoImpl.HomeRepositoryImpl
import org.koin.dsl.module

val HomeDiModule = module {
  single<HomeRepository> { HomeRepositoryImpl(routineDao = get(), sharedPreferencesRepository = get()) }
  single { AddReminderUseCase(routineRepository = get(), reminderScheduler = get()) }
  single { UpdateReminderUseCase(routineRepository = get(), reminderScheduler = get()) }
  single { CancelReminderUseCase(routineRepository = get(), reminderScheduler = get()) }
  single { GetTodayRoutinesUseCase(routineRepository = get()) }
  single { SearchRoutineUseCase(routineRepository = get()) }
  single { GetCurrentDateUseCase(homeRepository = get()) }
  single { DeleteReminderUseCase(routineRepository = get(), reminderScheduler = get()) }
}
