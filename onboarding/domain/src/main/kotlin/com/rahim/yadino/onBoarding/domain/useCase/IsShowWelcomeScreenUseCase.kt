package com.rahim.yadino.onBoarding.domain.useCase

import com.rahim.yadino.sharedPreferences.repo.SharedPreferencesRepository
import kotlinx.coroutines.flow.Flow

class IsShowWelcomeScreenUseCase(private val sharedPreferencesRepository: SharedPreferencesRepository) {
  operator fun invoke(): Flow<Boolean> = sharedPreferencesRepository.isShowWelcomeScreen()
}
