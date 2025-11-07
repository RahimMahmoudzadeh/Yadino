package com.rahim.yadino.onboarding.presentation.di

import com.rahim.yadino.onboarding.presentation.component.OnBoardingComponent
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val OnBoardingDiModule = module {
  viewModel { OnBoardingComponent(get()) }
}
