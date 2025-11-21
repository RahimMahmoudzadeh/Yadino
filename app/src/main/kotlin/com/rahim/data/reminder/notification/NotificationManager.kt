package com.rahim.data.reminder.notification

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.RemoteViews
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.rahim.R
import com.rahim.data.reminder.alarm.ControlAlarm
import com.rahim.ui.wakeup.WakeupActivity
import com.rahim.yadino.Constants
import com.rahim.yadino.Constants.ACTION_CANCEL_NOTIFICATION
import com.rahim.yadino.Constants.KEY_REMINDER_ALARM_ID
import com.rahim.yadino.Constants.NOTIFICATION_ID
import java.util.Random

class NotificationManager(private val controlAlarm: ControlAlarm) {
  fun createFullNotification(context: Context, routineName: String, routineIdAlarm: Int, routineExplanation: String) {
    val notificationId=Random().nextInt()
    val fullScreenIntent = Intent(context, WakeupActivity::class.java).apply {
      addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
      putExtra(Constants.REMINDER_NAME, routineName)
      putExtra(KEY_REMINDER_ALARM_ID, routineIdAlarm)
    }

    val dismissIntent = Intent(context, NotificationActionReceiver::class.java).apply {
      action = ACTION_CANCEL_NOTIFICATION
      putExtra(NOTIFICATION_ID, notificationId)
      putExtra(KEY_REMINDER_ALARM_ID, routineIdAlarm)
    }

    val dismissPendingIntent: PendingIntent = PendingIntent.getBroadcast(
      context,
      0,
      dismissIntent,
      PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
    )

    val customNotificationView = RemoteViews(context.packageName, R.layout.notification_layout)

    customNotificationView.setTextViewText(R.id.notification_title, routineName)
    customNotificationView.setTextViewText(R.id.notification_text, routineExplanation)

    customNotificationView.setOnClickPendingIntent(
      R.id.action_dismiss_button,
      dismissPendingIntent,
    )

    val fullScreenPendingIntent = PendingIntent.getActivity(
      context,
      0,
      fullScreenIntent,
      PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
    )

    controlAlarm.playRingtone(context, routineIdAlarm)

    val notificationBuilder =
      NotificationCompat.Builder(context, Constants.CHANNEL_ID)
        .setSmallIcon(com.rahim.yadino.library.designsystem.R.drawable.ic_round_notifications_24)
        .setContentTitle(routineName)
        .setContentText(routineExplanation)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setCategory(NotificationCompat.CATEGORY_ALARM)
        .setFullScreenIntent(fullScreenPendingIntent, true)
        .setDeleteIntent(dismissPendingIntent)
        .setStyle(NotificationCompat.DecoratedCustomViewStyle())
        .setCustomContentView(customNotificationView)
        .setCustomBigContentView(customNotificationView)
        .setCustomHeadsUpContentView(customNotificationView)

    with(NotificationManagerCompat.from(context)) {
      if (ActivityCompat.checkSelfPermission(
          context,
          Manifest.permission.POST_NOTIFICATIONS,
        ) != PackageManager.PERMISSION_GRANTED
      ) {
        return
      }
      notify(notificationId, notificationBuilder.build())
    }
  }
}
