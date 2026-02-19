package com.rahim.yadino.sharedPreferences.useCase

import com.rahim.yadino.sharedPreferences.repo.SharedPreferencesRepository
import kotlinx.coroutines.flow.Flow

class IsDarkThemeUseCase(private val sharedPreferencesRepository: SharedPreferencesRepository) {
  operator fun invoke(): Flow<Boolean?> = sharedPreferencesRepository.isDarkTheme()
}
