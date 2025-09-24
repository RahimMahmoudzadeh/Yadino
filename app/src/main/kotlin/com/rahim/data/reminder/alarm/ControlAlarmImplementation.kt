package com.rahim.data.reminder.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.CountDownTimer
import com.rahim.data.reminder.YadinoBroadCastReceiver

class ControlAlarmImplementation: ControlAlarm {
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
