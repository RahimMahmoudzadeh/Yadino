package com.rahim.yadino.core.timeDate.di

import com.rahim.yadino.core.timeDate.repo.DateTimeRepository
import com.rahim.yadino.core.timeDate.repo.DateTimeRepositoryImpl
import com.rahim.yadino.core.timeDate.useCase.CalculateTodayUseCase
import com.rahim.yadino.enums.DispatchersQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module

val TimeDateDiModule = module {
  single<DateTimeRepository> { DateTimeRepositoryImpl(defaultDispatcher = get(named(DispatchersQualifier.Default)), ioDispatcher = get(named(DispatchersQualifier.Default)), timeDao = get()) }
  single { CalculateTodayUseCase(dateTimeRepository = get()) }
}
