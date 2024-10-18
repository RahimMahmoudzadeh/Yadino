package com.rahim.yadino.sharedPreferences

interface SharedPreferencesRepository {
  fun saveShowWelcome(isShow: Boolean)

  fun isShowWelcomeScreen(): Boolean

  fun setShowSampleRoutine(isShow: Boolean)
  fun isShowSampleRoutine(): Boolean
  fun isShowSampleNote(isShow: Boolean)
  fun changeTheme(isDarkTheme: String)
  fun isDarkTheme(): String?
}
