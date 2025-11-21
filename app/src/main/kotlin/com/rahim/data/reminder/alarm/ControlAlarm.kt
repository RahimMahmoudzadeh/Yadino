package com.rahim.data.reminder.alarm

import android.content.Context
import android.media.Ringtone

interface ControlAlarm {
  fun cancelAlarm(context: Context, idAlarm: Int?)
  fun playRingtone(context: Context, alarmId: Int?)
  fun stopRingtone(context: Context, alarmId: Int?)
}
