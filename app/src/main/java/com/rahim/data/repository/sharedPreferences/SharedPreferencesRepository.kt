package com.rahim.data.repository.sharedPreferences

interface SharedPreferencesRepository {
    fun saveShowWelcome(isShow: Boolean)

    fun isShowWelcomeScreen(): Boolean
}