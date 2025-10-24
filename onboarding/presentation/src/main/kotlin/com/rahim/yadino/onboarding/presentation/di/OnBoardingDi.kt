package com.rahim.yadino.onboarding.presentation.di

import com.rahim.yadino.onboarding.presentation.OnBoardingViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val OnBoardingDiModule = module {
  viewModel { OnBoardingViewModel(get()) }
}
