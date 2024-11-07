package com.rahim.yadino

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object Constants {
    //Static
    const val CHANNEL_ID = "Yadinoo"
    const val CHANNEL_NAME = "Main Channel"
    const val NAME_SHARED_PREFERENCE = "YadinooShared"
    const val UPDATE_VERSION = "update version"
    const val IS_FORCE = "is force"
    const val VERSION = "version"
    const val YYYY_MM_DD = "yyyy-MM-dd"
    const val FIRST_YEAR = 1350
    const val END_YEAR = 1450
    const val FIRST_KABISE_DATA = 1350
    const val VERSION_TIME_DB = 1L
    const val UPDATE = "update"
    const val CAFE_BAZAAR = "cafeBazaar"
    const val CAFE_BAZZAR_LINK="https://cafebazaar.ir/app/com.rahim.yadino"
    const val GOOGLE_PLAY_LINK="https://play.google.com/store/apps/details?id=com.rahim.yadino&hl=en"
    const val MY_KET_LINK="https://myket.ir/app/com.rahim.yadino"
    const val CAFE_BAZAAR_PACKAGE_NAME="com.farsitel.bazaar"
    const val MY_KET_PACKAGE_NAME="ir.mservices.market"
    const val DARK="dark theme"
    const val LIGHT="light theme"
    const val PATTERN_DATE="yyyy-MM-dd HH:mm:ss a"

    // DataStore keys
    val WELCOME_SHARED = booleanPreferencesKey("WelcomeShared")
    val SAMPLE_ROUTINE = booleanPreferencesKey("sampleRoutine")
    val SAMPLE_NOTE = booleanPreferencesKey("sample note")
    val IS_DARK_THEME= stringPreferencesKey("is dark theme")

    //Action
    const val ACTION_SEND_NOTIFICATION="action send notification"
    const val ACTION_CANCEL_NOTIFICATION="action cancel notification"

    //Key
    const val ALARM_MESSAGE = "alarm Message"
    const val ALARM_NAME = "alarm name"
    const val ALARM_ID = "alarmId"
    const val ROUTINE = "routine"
    const val KEY_LAUNCH_NAME = "key_launch_name"
    const val KEY_LAUNCH_ID = "key_reminder_name"

    //Value
    const val IS_ALARM = "is Alarm"
}
