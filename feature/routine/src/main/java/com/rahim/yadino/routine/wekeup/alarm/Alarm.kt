package com.rahim.yadino.routine.wekeup.alarm

import android.content.Context

interface Alarm {
    fun cancelAlarm(context: Context, idAlarm: Long?)
}