package com.example.sharedPreferences

import com.rahim.yadino.sharedPreferences.SharedPreferencesCustom
import com.rahim.yadino.sharedPreferences.SharedPreferencesRepository
import javax.inject.Inject

class SharedPreferencesRepositoryImpl @Inject constructor(private val sharedPreferencesCustom: SharedPreferencesCustom) :
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
