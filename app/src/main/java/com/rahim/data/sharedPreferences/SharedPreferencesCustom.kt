package com.rahim.data.sharedPreferences

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.rahim.utils.Constants.NAME_SHARED_PREFERENCE
import com.rahim.utils.Constants.WELCOME_SHARED
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
}