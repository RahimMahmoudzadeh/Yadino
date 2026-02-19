package com.rahim.yadino.sharedPreferences.useCase

import com.rahim.yadino.sharedPreferences.repo.SharedPreferencesRepository

class IsDarkThemeUseCase(private val sharedPreferencesRepository: SharedPreferencesRepository) {
  operator fun invoke() = sharedPreferencesRepository.isDarkTheme()
}
