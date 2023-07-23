package com.rahim.data.broadcast

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import com.rahim.data.notification.NotificationManager
import com.rahim.utils.Constants.ALARM_MESSAGE
import com.rahim.utils.Constants.ALARM_NAME
import timber.log.Timber


class YadinoBroadCastReceiver() : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        sendNotificationAlarm(intent, context)
        Timber.tag("alarmReceiver").d(intent?.getStringExtra(ALARM_MESSAGE))

    }

    private fun sendNotificationAlarm(intent: Intent?, context: Context?) {
        intent?.extras?.getString(ALARM_MESSAGE)?.let {
            val message=it
            val name=intent.extras?.getString(ALARM_NAME)
            val nothing = NotificationManager()
            context?.let {
                nothing.createFullNotification(name.toString(), message, it)
            }
        }
    }

    interface Receiver {
        fun onReceive(context: Context?, intent: Intent?)
    }

}