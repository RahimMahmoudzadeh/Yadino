package com.rahim.utils

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.rahim.BuildConfig
import com.rahim.R
import com.rahim.yadino.base.Constants.CHANNEL_ID
import com.rahim.yadino.base.Constants.CHANNEL_NAME
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        createNotificationChannel()
        setFirebaseAnalyse()
    }
    private fun setFirebaseAnalyse() {
        FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(!BuildConfig.DEBUG)
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
    }
    private fun createNotificationChannel() {
        val descriptionText = getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}