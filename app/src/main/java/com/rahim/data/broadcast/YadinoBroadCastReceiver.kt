package com.rahim.data.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.rahim.yadino.routine.modle.Rotin.Routine
import com.rahim.data.notification.NotificationManager
import com.rahim.yadino.base.Constants.ALARM_MESSAGE
import com.rahim.yadino.base.Constants.ROUTINE
import timber.log.Timber


class YadinoBroadCastReceiver() : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        sendNotificationAlarm(intent, context)
        Timber.tag("alarmReceiver").d(intent?.getStringExtra(ALARM_MESSAGE))

    }

    private fun sendNotificationAlarm(intent: Intent?, context: Context?) {
        intent?.extras?.let {extras->
            val routine=if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                extras.getParcelable(ROUTINE, com.rahim.yadino.routine.modle.Rotin.Routine::class.java)
            }else{
                extras.getParcelable(ROUTINE)
            }
            val nothing = NotificationManager()
            context?.let {
                Timber.tag("intentTitle").d("YadinoBroadCastReceiver-> ${routine?.name}")
                nothing.createFullNotification(context,routine)
            }
        }
    }
}