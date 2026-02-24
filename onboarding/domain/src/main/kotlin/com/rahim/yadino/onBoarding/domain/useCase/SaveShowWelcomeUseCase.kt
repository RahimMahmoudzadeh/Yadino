package com.rahim.yadino.onBoarding.domain.useCase

import com.rahim.yadino.sharedPreferences.repo.SharedPreferencesRepository

class SaveShowWelcomeUseCase(private val sharedPreferencesRepository: SharedPreferencesRepository) {
  suspend operator fun invoke() = sharedPreferencesRepository.saveShowWelcome()
}
