package com.rahim.data.alarm

import android.content.Context

interface Alarm {
    fun cancelAlarm(context: Context, idAlarm:Int?)
}