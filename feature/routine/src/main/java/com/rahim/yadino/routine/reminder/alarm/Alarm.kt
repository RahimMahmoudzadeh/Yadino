package com.rahim.yadino.routine.reminder.alarm

import android.content.Context

interface Alarm {
    fun cancelAlarm(context: Context, idAlarm: Long?)
}