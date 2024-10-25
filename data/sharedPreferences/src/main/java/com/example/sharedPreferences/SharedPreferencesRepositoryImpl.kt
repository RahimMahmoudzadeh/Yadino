package com.example.sharedPreferences

import com.rahim.yadino.sharedPreferences.SharedPreferencesCustom
import com.rahim.yadino.sharedPreferences.SharedPreferencesRepository
import javax.inject.Inject

class SharedPreferencesRepositoryImpl @Inject
constructor(private val sharedPreferencesCustom: SharedPreferencesCustom) :
  SharedPreferencesRepository {
  override fun saveShowWelcome(isShow: Boolean) {
    sharedPreferencesCustom.saveWelcomePage(isShow)
  }

  override fun isShowWelcomeScreen(): Boolean = sharedPreferencesCustom.isShowWelcome()
  override fun setShowSampleRoutine(isShow: Boolean) {
    if (isShowSampleRoutine()) return
    sharedPreferencesCustom.showSampleRoutine(isShow)
  }

  override fun isShowSampleRoutine(): Boolean = sharedPreferencesCustom.isShowSampleRoutine()
  override fun isShowSampleNote() = sharedPreferencesCustom.isSampleNote()

  override fun setShowSampleNote(isShow: Boolean) {
    if (isShowSampleNote()) return
    sharedPreferencesCustom.showSampleNote(isShow)
  }

  override fun changeTheme(isDarkTheme: String) {
    sharedPreferencesCustom.setDarkTheme(isDarkTheme)
  }

  override fun isDarkTheme(): String? = sharedPreferencesCustom.isDarkTheme()
}
