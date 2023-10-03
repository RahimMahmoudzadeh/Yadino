package com.rahim.data.service.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.rahim.R
import com.rahim.data.sharedPreferences.SharedPreferencesCustom
import com.rahim.ui.main.MainActivity
import com.rahim.utils.Constants.CHANNEL_ID
import com.rahim.utils.Constants.IS_FORCE
import com.rahim.utils.Constants.UPDATE
import com.rahim.utils.Constants.VERSION
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.Random
import javax.inject.Inject


@AndroidEntryPoint
class FirebaseNotificationService : FirebaseMessagingService() {
    @Inject
    lateinit var sharedPreferencesCustom: SharedPreferencesCustom
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        remoteMessage.data?.let {
            if (remoteMessage.data.containsKey(UPDATE)) {
                val isForce = remoteMessage.data.getValue(UPDATE)
                val version = remoteMessage.data.getValue(VERSION)
                sharedPreferencesCustom.sendNotificationUpdate(
                    true,
                    isForce.contains(IS_FORCE),
                    version.toInt()
                )
            }
            val pendingIntent =
                PendingIntent.getActivity(
                    this,
                    0,
                    Intent(this, MainActivity::class.java),
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            remoteMessage.notification?.let {
                var builder = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher_foreground)
                    .setContentTitle(it.title)
                    .setContentText(it.body)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                with(NotificationManagerCompat.from(this)) {
                    if (ActivityCompat.checkSelfPermission(
                            this@FirebaseNotificationService,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return
                    }
                    notify(Random().nextInt(), builder.build())
                }
            }
        }
    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()
    }
}