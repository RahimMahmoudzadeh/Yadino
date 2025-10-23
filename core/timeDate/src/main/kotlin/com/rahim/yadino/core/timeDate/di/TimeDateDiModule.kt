package com.rahim.yadino.core.timeDate.di

import com.rahim.yadino.core.timeDate.repo.DateTimeRepository
import com.rahim.yadino.core.timeDate.repo.DateTimeRepositoryImpl
import org.koin.dsl.module

val TimeDateDiModule = module {
  single<DateTimeRepository> { DateTimeRepositoryImpl(defaultDispatcher = get(), ioDispatcher = get(), timeDao = get()) }
}
