package com.rahim.home.domain.alarm

import android.content.Context

interface Alarm {
  fun cancelAlarm(context: Context, idAlarm: Long?)
}
