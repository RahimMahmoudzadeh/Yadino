package com.rahim.data.repository.sharedPreferences

interface SharedPreferencesRepository {
    fun saveShowWelcome(isShow: Boolean)

    fun isShowWelcomeScreen(): Boolean

    fun isShowSampleRoutine(isShow: Boolean)
    fun isShowSampleNote(isShow: Boolean)
    fun changeTheme(isDarkTheme: String)
    fun isDarkTheme(): String?
}