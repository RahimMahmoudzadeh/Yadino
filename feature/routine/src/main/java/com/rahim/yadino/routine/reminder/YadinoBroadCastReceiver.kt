package com.rahim.yadino.routine.reminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.rahim.yadino.Constants.KEY_LAUNCH_ID
import com.rahim.yadino.Constants.KEY_LAUNCH_NAME
import timber.log.Timber


class YadinoBroadCastReceiver() : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Timber.tag("intentTitle").d("YadinoBroadCastReceiver onReceive-> ${intent?.action}")
        sendNotificationAlarm(intent, context)
//        Timber.tag("alarmReceiver").d(intent?.getStringExtra(ALARM_MESSAGE))

    }

    //
//    private fun sendNotificationAlarm(intent: Intent?, context: Context?) {
//        intent?.extras?.let { extras ->
//            val reminderName = extras.getString(KEY_LAUNCH_NAME)
//            val reminderId = extras.getInt(KEY_LAUNCH_ID, 0)
//            Timber.tag("yadinoBroadcast").d("YadinoBroadCastReceiver reminderName->$reminderName")
//            Timber.tag("yadinoBroadcast").d("YadinoBroadCastReceiver reminderId->$reminderId")
//            Intent().also { intent ->
//                intent.setAction(ACTION_SEND_NOTIFICATION)
//                intent.putExtra(KEY_LAUNCH_ID, reminderId)
//                intent.putExtra(KEY_LAUNCH_NAME, reminderName)
//                intent.setPackage(context?.packageName)
//                context?.sendBroadcast(intent)
//            }
//        }
//    }
    private fun sendNotificationAlarm(intent: Intent?, context: Context?) {
        intent?.extras?.let { extras ->
            val reminderName = extras.getString(KEY_LAUNCH_NAME)
            val reminderId = extras.getInt(KEY_LAUNCH_ID, 0)
            val nothing = NotificationManager()
            context?.let {
                Timber.tag("intentTitle").d("YadinoBroadCastReceiver-> ${reminderName}")
                nothing.createFullNotification(
                    context,
                    reminderName ?: "",
                    reminderId.toLong() ?: 0L,
                    ""
                )
            }
        }
    }

}
