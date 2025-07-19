package com.rahim.yadino.sharedPreferences.repo

import com.rahim.yadino.sharedPreferences.SharedPreferencesCustom
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SharedPreferencesRepositoryImpl @Inject
constructor(private val sharedPreferencesCustom: SharedPreferencesCustom) :
  SharedPreferencesRepository {
  override suspend fun saveShowWelcome(isShow: Boolean) {
    sharedPreferencesCustom.saveWelcomePage(isShow)
  }

  override fun isShowWelcomeScreen() = sharedPreferencesCustom.isShowWelcome()
  override suspend fun setShowSampleRoutine(isShow: Boolean) {
    if (isShowSampleRoutine().first()) return
    sharedPreferencesCustom.showSampleRoutine(isShow)
  }

  override fun isShowSampleRoutine(): Flow<Boolean> = sharedPreferencesCustom.isShowSampleRoutine()
  override fun isShowSampleNote() = sharedPreferencesCustom.isSampleNote()

  override suspend fun setShowSampleNote(isShow: Boolean) {
    if (isShowSampleNote().first()) return
    sharedPreferencesCustom.showSampleNote(isShow)
  }

  override suspend fun changeTheme(isDarkTheme: Boolean) {
    sharedPreferencesCustom.setDarkTheme(isDarkTheme)
  }

  override fun isDarkTheme(): Flow<Boolean?> = sharedPreferencesCustom.isDarkTheme()
}
