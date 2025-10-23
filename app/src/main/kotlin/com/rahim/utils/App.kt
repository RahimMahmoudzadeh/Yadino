package com.rahim.utils

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.rahim.BuildConfig
import com.rahim.R
import com.rahim.yadino.Constants.CHANNEL_ID
import com.rahim.yadino.Constants.CHANNEL_NAME
import com.rahim.yadino.home.data.di.homeDiModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {
  override fun onCreate() {
    super.onCreate()

    startKoin {
      androidContext(this@App)
      modules(homeDiModule)
    }

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
