package com.rahim.yadino.sharedPreferences

interface SharedPreferencesRepository {
  fun saveShowWelcome(isShow: Boolean)

  fun isShowWelcomeScreen(): Boolean

  fun setShowSampleRoutine(isShow: Boolean = true)
  fun isShowSampleRoutine(): Boolean
  fun isShowSampleNote(): Boolean
  fun setShowSampleNote(isShow: Boolean = true)
  fun changeTheme(isDarkTheme: String)
  fun isDarkTheme(): String?
}
