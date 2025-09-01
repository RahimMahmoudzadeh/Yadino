package com.rahim.yadino.home.presentation

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.CountDownTimer
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.rahim.home.domain.YadinoBroadCastReceiver
import com.rahim.home.domain.alarm.Alarm
import com.rahim.home.domain.alarm.AlarmSong
import com.rahim.yadino.Constants
import com.rahim.yadino.library.designsystem.R
import java.util.Random
import javax.inject.Inject

class NotificationManager @Inject constructor() : AlarmSong, Alarm {
//    fun createNotification(textTitle: String, textContent: String, context: Context) {
//        val intent = Intent(context, MainActivity::class.kotlin).apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        }
//        val pendingIntent: PendingIntent =
//            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
//
//        var builder = NotificationCompat.Builder(context, CHANNEL_ID)
//            .setSmallIcon(com.rahim.yadino.library.designsystem.R.drawable.ic_round_notifications_24)
//            .setContentTitle(textTitle)
//            .setContentText(textContent)
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//            .setContentIntent(pendingIntent)
//            .setAutoCancel(true)
//            .setSound(null)
//            .build()
//
//        with(NotificationManagerCompat.from(context)) {
//            if (ActivityCompat.checkSelfPermission(
//                    context,
//                    Manifest.permission.POST_NOTIFICATIONS
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                return
//            }
//            notify(Random().nextInt(), builder)
//        }
//    }

  fun createFullNotification(context: Context, routineName: String, routineIdAlarm: Long, routineExplanation: String) {
    val fullScreenIntent = Intent(context, WakeupActivity::class.java).apply {
      Intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        Intent.putExtra(Constants.KEY_LAUNCH_NAME, routineName)
    }
    val fullScreenPendingIntent = PendingIntent.getActivity(
      context,
      0,
      fullScreenIntent,
      PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
    )
    playRingtone(context, routineIdAlarm)
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

  override fun playRingtone(context: Context, alarmId: Long?) {
    val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
    val ringtone = RingtoneManager.getRingtone(context, notification)
    ringtone.play()
    stopRingtone(ringtone, context, alarmId)
  }

  override fun stopRingtone(ringtone: Ringtone?, context: Context, alarmId: Long?) {
    object : CountDownTimer(6000, 1000) {
      override fun onTick(millisUntilFinished: Long) {}
      override fun onFinish() {
        ringtone?.stop()
        cancelAlarm(context, alarmId)
      }
    }.start()
  }

  override fun cancelAlarm(context: Context, idAlarm: Long?) {
    val intent = Intent(context, YadinoBroadCastReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
      context,
      idAlarm?.toInt() ?: 0,
      intent,
      PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
    )
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
    alarmManager!!.cancel(pendingIntent)
  }
}
