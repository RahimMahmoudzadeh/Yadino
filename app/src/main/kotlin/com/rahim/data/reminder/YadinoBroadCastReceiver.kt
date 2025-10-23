package com.rahim.data.reminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.rahim.yadino.Constants

class YadinoBroadCastReceiver(private val notificationManager: NotificationManager) : BroadcastReceiver() {
  override fun onReceive(context: Context?, intent: Intent?) {
    sendNotificationAlarm(intent, context)
  }

  private fun sendNotificationAlarm(intent: Intent?, context: Context?) {
    intent?.extras?.let { extras ->
      val reminderName = extras.getString(Constants.KEY_LAUNCH_NAME)
      val reminderId = extras.getInt(Constants.KEY_LAUNCH_ID, 0)

      context?.let {
        notificationManager.createFullNotification(
          context,
          reminderName ?: "",
          reminderId.toLong(),
          "",
        )
      }
    }
  }
}
