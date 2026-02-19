package com.rahim.yadino.sharedPreferences.useCase

import com.rahim.yadino.sharedPreferences.repo.SharedPreferencesRepository

class ChangeThemeUseCase(private val sharedPreferencesRepository: SharedPreferencesRepository) {
  suspend operator fun invoke(isDarkTheme: Boolean) {
    sharedPreferencesRepository.changeTheme(isDarkTheme)
  }
}
