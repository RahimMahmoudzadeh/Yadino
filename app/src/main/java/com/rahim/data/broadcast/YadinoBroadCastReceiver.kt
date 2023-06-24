package com.rahim.data.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.rahim.data.notification.NotificationManager
import com.rahim.utils.Constants.ALARM_MESSAGE
import timber.log.Timber

class YadinoBroadCastReceiver() : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        sendNotificationAlarm(intent, context)
        Timber.tag("alarmReceiver").d(intent?.getStringExtra(ALARM_MESSAGE))

    }

    private fun sendNotificationAlarm(intent: Intent?, context: Context?) {
        intent?.extras?.getString(ALARM_MESSAGE)?.let {
            val nothing = NotificationManager()
            context?.let {
                nothing.createNotification("isAlarm", "ok", it)
            }
        }
    }

    interface Receiver {
        fun onReceive(context: Context?, intent: Intent?)
    }

}