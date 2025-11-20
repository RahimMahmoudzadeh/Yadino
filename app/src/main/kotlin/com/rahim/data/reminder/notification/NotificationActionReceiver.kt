package com.rahim.data.reminder.notification

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import com.rahim.data.reminder.alarm.ControlAlarm
import com.rahim.yadino.Constants.ACTION_CANCEL_NOTIFICATION
import com.rahim.yadino.Constants.KEY_REMINDER_ID
import com.rahim.yadino.Constants.NOTIFICATION_ID
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NotificationActionReceiver : BroadcastReceiver(),KoinComponent {

  private val controlAlarm: ControlAlarm by inject()

  override fun onReceive(context: Context, intent: Intent) {
    if (intent.action == ACTION_CANCEL_NOTIFICATION) {
      val notificationId = intent.getIntExtra(NOTIFICATION_ID, -1)
      val routineId = intent.getIntExtra(KEY_REMINDER_ID, -1)
      if (notificationId != -1) {
        with(NotificationManagerCompat.from(context)) {
          if (ActivityCompat.checkSelfPermission(
              context,
              Manifest.permission.POST_NOTIFICATIONS,
            ) != PackageManager.PERMISSION_GRANTED
          ) {
            return
          }
          controlAlarm.cancelAlarm(context,routineId)
          cancel(notificationId)
        }
      }
    }
  }
}
