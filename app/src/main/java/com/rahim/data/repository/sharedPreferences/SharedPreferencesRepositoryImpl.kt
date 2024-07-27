package com.rahim.data.repository.sharedPreferences

import com.rahim.yadino.base.sharedPreferences.SharedPreferencesCustom
import javax.inject.Inject

class SharedPreferencesRepositoryImpl @Inject constructor(private val sharedPreferencesCustom: com.rahim.yadino.base.sharedPreferences.SharedPreferencesCustom) :
    SharedPreferencesRepository {
    override fun saveShowWelcome(isShow: Boolean) {
        sharedPreferencesCustom.saveWelcomePage(isShow)
    }

    override fun isShowWelcomeScreen(): Boolean = sharedPreferencesCustom.isShowWelcome()
    override fun isShowSampleRoutine(isShow: Boolean) {
        sharedPreferencesCustom.showSampleRoutine(isShow)
    }

    override fun isShowSampleNote(isShow: Boolean) {
        sharedPreferencesCustom.showSampleNote(isShow)
    }

    override fun changeTheme(isDarkTheme: String) {
        sharedPreferencesCustom.setDarkTheme(isDarkTheme)
    }

    override fun isDarkTheme(): String? = sharedPreferencesCustom.isDarkTheme()
}