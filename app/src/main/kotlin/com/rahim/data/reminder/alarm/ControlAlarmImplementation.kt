package com.rahim.data.reminder.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import com.rahim.data.reminder.YadinoBroadCastReceiver

class ControlAlarmImplementation : ControlAlarm {

  private var ringtone: Ringtone? = null

  override fun playRingtone(context: Context, alarmId: Long?) {
    ringtone?.stop()
    val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
    ringtone = RingtoneManager.getRingtone(context, notification)
    ringtone?.play()
  }

  override fun stopRingtone(context: Context, alarmId: Long?) {
    ringtone?.stop()
    cancelAlarm(context = context, alarmId = alarmId)
  }

  override fun cancelAlarm(context: Context, alarmId: Long?) {
    val intent = Intent(context, YadinoBroadCastReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
      context,
      alarmId?.toInt() ?: 0,
      intent,
      PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
    )
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
    alarmManager!!.cancel(pendingIntent)
  }
}
