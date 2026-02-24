package com.rahim.yadino.onboarding.presentation.di

import com.rahim.yadino.onBoarding.domain.useCase.IsShowWelcomeScreenUseCase
import com.rahim.yadino.onBoarding.domain.useCase.SaveShowWelcomeUseCase
import org.koin.dsl.module

val onBoardingDiModule = module {
  single { IsShowWelcomeScreenUseCase(sharedPreferencesRepository = get()) }
  single { SaveShowWelcomeUseCase(sharedPreferencesRepository = get()) }
}
