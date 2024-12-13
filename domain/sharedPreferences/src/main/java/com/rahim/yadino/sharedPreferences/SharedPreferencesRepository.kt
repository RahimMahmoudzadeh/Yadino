package com.rahim.yadino.sharedPreferences

import kotlinx.coroutines.flow.Flow

interface SharedPreferencesRepository {
  suspend fun saveShowWelcome(isShow: Boolean)
  fun isShowWelcomeScreen(): Flow<Boolean>
  suspend fun setShowSampleRoutine(isShow: Boolean = true)
  fun isShowSampleRoutine(): Flow<Boolean>
  fun isShowSampleNote(): Flow<Boolean>
  suspend fun setShowSampleNote(isShow: Boolean = true)
  suspend fun changeTheme(isDarkTheme: Boolean)
  fun isDarkTheme(): Flow<Boolean?>
}
