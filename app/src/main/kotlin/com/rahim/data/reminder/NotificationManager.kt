package com.rahim.data.reminder

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.rahim.data.reminder.alarm.ControlAlarm
import com.rahim.ui.wakeup.WakeupActivity
import com.rahim.yadino.Constants
import com.rahim.yadino.library.designsystem.R
import java.util.Random
import javax.inject.Inject

class NotificationManager @Inject constructor(private val controlAlarm:ControlAlarm){
  fun createFullNotification(context: Context, routineName: String, routineIdAlarm: Long, routineExplanation: String) {
    val fullScreenIntent = Intent(context, WakeupActivity::class.java).apply {
      addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
      putExtra(Constants.KEY_LAUNCH_NAME, routineName)
    }
    val fullScreenPendingIntent = PendingIntent.getActivity(
      context,
      0,
      fullScreenIntent,
      PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
    )
    controlAlarm.playRingtone(context, routineIdAlarm)
    val notificationBuilder =
      NotificationCompat.Builder(context, Constants.CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_round_notifications_24)
        .setContentTitle(routineName)
        .setContentText(routineExplanation)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setCategory(NotificationCompat.CATEGORY_ALARM)
        .setFullScreenIntent(fullScreenPendingIntent, true)

    with(NotificationManagerCompat.from(context)) {
      if (ActivityCompat.checkSelfPermission(
          context,
          Manifest.permission.POST_NOTIFICATIONS,
        ) != PackageManager.PERMISSION_GRANTED
      ) {
        return
      }
      notify(Random().nextInt(), notificationBuilder.build())
    }
  }
}
