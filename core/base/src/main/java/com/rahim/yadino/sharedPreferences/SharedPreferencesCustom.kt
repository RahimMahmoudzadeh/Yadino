package com.rahim.yadino.sharedPreferences

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.rahim.yadino.Constants.IS_DARK_THEME
import com.rahim.yadino.Constants.NAME_SHARED_PREFERENCE
import com.rahim.yadino.Constants.SAMPLE_NOTE
import com.rahim.yadino.Constants.SAMPLE_ROUTINE
import com.rahim.yadino.Constants.WELCOME_SHARED
import javax.inject.Inject


class SharedPreferencesCustom @Inject constructor(private val context: Context) {
    private var sharedPreferences =
        context.getSharedPreferences(NAME_SHARED_PREFERENCE, MODE_PRIVATE)
    private var edit = sharedPreferences.edit()

    fun saveWelcomePage(isShow: Boolean) {
        edit.putBoolean(WELCOME_SHARED, isShow)
        edit.apply()
    }

    fun isShowWelcome() = sharedPreferences.getBoolean(WELCOME_SHARED, false)

    fun showSampleRoutine(isShow: Boolean) {
        edit.putBoolean(SAMPLE_ROUTINE, isShow)
        edit.apply()
    }

    fun isShowSampleRoutine() = sharedPreferences.getBoolean(SAMPLE_ROUTINE, false)
    fun showSampleNote(isShow: Boolean) {
        edit.putBoolean(SAMPLE_NOTE, isShow)
        edit.apply()
    }

    fun isSampleNote() = sharedPreferences.getBoolean(SAMPLE_NOTE, false)

    fun setDarkTheme(isDarkTheme: String) {
        edit.putString(IS_DARK_THEME, isDarkTheme)
        edit.apply()
    }
    fun isDarkTheme() = sharedPreferences.getString(IS_DARK_THEME,null)

}
