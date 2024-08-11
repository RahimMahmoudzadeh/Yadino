package com.rahim.yadino.base.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.rahim.yadino.base.Constants.ACTION_SEND_NOTIFICATION
import com.rahim.yadino.base.Constants.ALARM_MESSAGE
import com.rahim.yadino.base.Constants.KEY_LAUNCH_ID
import com.rahim.yadino.base.Constants.KEY_LAUNCH_NAME
import timber.log.Timber


class YadinoBroadCastReceiver() : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        sendNotificationAlarm(intent, context)
//        Timber.tag("alarmReceiver").d(intent?.getStringExtra(ALARM_MESSAGE))

    }

    private fun sendNotificationAlarm(intent: Intent?, context: Context?) {
        intent?.extras?.let { extras ->
            val reminderName = extras.getString(KEY_LAUNCH_NAME)
            val reminderId = extras.getInt(KEY_LAUNCH_ID, 0)
            Timber.tag("yadinoBroadcast").d("YadinoBroadCastReceiver reminderName->$reminderName")
            Timber.tag("yadinoBroadcast").d("YadinoBroadCastReceiver reminderId->$reminderId")

        }
    }

}