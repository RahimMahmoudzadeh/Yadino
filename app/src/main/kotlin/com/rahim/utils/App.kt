package com.rahim.utils

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.rahim.BuildConfig
import com.rahim.R
import com.rahim.di.CafeBazaarDiModule
import com.rahim.di.YadinoDiModule
import com.rahim.yadino.Constants.CHANNEL_ID
import com.rahim.yadino.Constants.CHANNEL_NAME
import com.rahim.yadino.core.timeDate.di.TimeDateDiModule
import com.rahim.yadino.db.di.YadinoDatabaseModule
import com.rahim.yadino.di.CoreDiModule
import com.rahim.yadino.home.data.di.HomeDiModule
import com.rahim.yadino.home.presentation.di.HomeDiPresentationModule
import com.rahim.yadino.note.data.di.NoteDiModule
import com.rahim.yadino.note.presentation.di.NotePresentationDiModule
import com.rahim.yadino.onboarding.presentation.di.OnBoardingDiModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {
  override fun onCreate() {
    super.onCreate()

    startKoin {
      androidContext(this@App)
      modules(
        YadinoDiModule,
        HomeDiModule,
        HomeDiPresentationModule,
        CafeBazaarDiModule,
        YadinoDatabaseModule,
        TimeDateDiModule,
        CoreDiModule,
        NoteDiModule,
        NotePresentationDiModule,
        OnBoardingDiModule,
      )
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
