package com.rahim.data.notification

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.media.Ringtone
import android.media.RingtoneManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.rahim.R
import com.rahim.data.alarm.Alarm
import com.rahim.data.alarm.AlarmManagement
import com.rahim.data.alarm.AlarmSong
import com.rahim.data.broadcast.YadinoBroadCastReceiver
import com.rahim.ui.main.MainActivity
import com.rahim.ui.wakeup.WakeupActivity
import com.rahim.utils.Constants.CHANNEL_ID
import com.rahim.utils.Constants.TITLE_TASK
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Random
import javax.inject.Inject


class NotificationManager @Inject constructor() : AlarmSong, Alarm {

    fun createNotification(textTitle: String, textContent: String, context: Context) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        var builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_round_notifications_24)
            .setContentTitle(textTitle)
            .setContentText(textContent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setSound(null)
            .build()

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(Random().nextInt(), builder)
        }
    }

    fun createFullNotification(textTitle: String, textContent: String, context: Context,alarmId:Int?) {
        Timber.tag("intentTitle").d("notification-> $textTitle")

        val fullScreenIntent = Intent(context, WakeupActivity::class.java).apply {
            putExtra(TITLE_TASK, textTitle)
        }
        val fullScreenPendingIntent = PendingIntent.getActivity(
            context, 0,
            fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        playRingtone(context,alarmId)
        val notificationBuilder =
            NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_round_notifications_24)
                .setContentTitle(textTitle)
                .setContentText(textContent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setFullScreenIntent(fullScreenPendingIntent, true)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(Random().nextInt(), notificationBuilder.build())
        }
    }

    override fun playRingtone(context: Context,alarmId:Int?) {
        val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val ringtone = RingtoneManager.getRingtone(context, notification)
        ringtone.play()
        stopRingtone(ringtone,context,alarmId)
    }

    override fun stopRingtone(ringtone: Ringtone?,context:Context,alarmId: Int?) {
        CoroutineScope(Dispatchers.IO).launch {
            delay(6000)
            ringtone?.stop()
            cancelAlarm(context,alarmId)
        }
    }

    override fun cancelAlarm(context: Context,idAlarm:Int?) {
        val intent = Intent(context, YadinoBroadCastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, idAlarm?:0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager?
        alarmManager!!.cancel(pendingIntent)
    }
}