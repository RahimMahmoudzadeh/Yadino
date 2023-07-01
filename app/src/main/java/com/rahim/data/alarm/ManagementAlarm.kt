package com.rahim.data.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.rahim.data.broadcast.YadinoBroadCastReceiver
import com.rahim.utils.Constants.ALARM_MESSAGE
import com.rahim.utils.Constants.ALARM_NAME
import com.rahim.utils.Constants.IS_ALARM
import java.util.Calendar
import kotlin.random.Random

class ManagementAlarm {

    fun setAlarm(context: Context, hours: Int, minute: Int,name:String,message:String) {
        val calendar: Calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hours)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, YadinoBroadCastReceiver::class.java).let { intent ->
            intent.putExtra(ALARM_MESSAGE, message)
            intent.putExtra(ALARM_NAME, name)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )


        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }
}