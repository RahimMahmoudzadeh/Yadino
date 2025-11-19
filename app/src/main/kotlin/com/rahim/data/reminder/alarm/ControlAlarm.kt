package com.rahim.data.reminder.alarm

import android.content.Context
import android.media.Ringtone

interface ControlAlarm {
  fun cancelAlarm(context: Context, idAlarm: Long?)
  fun playRingtone(context: Context, alarmId: Long?)
  fun stopRingtone(context: Context, alarmId: Long?)
}
