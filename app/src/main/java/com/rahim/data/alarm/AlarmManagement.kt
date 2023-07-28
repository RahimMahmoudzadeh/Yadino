package com.rahim.data.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.provider.AlarmClock
import com.rahim.data.broadcast.YadinoBroadCastReceiver
import com.rahim.data.date.CalculateDate
import com.rahim.utils.Constants.ALARM_ID
import com.rahim.utils.Constants.ALARM_MESSAGE
import com.rahim.utils.Constants.ALARM_NAME
import saman.zamani.persiandate.PersianDate
import timber.log.Timber
import java.util.Calendar
import java.util.Random

 class AlarmManagement : CalculateDate,Alarm {
    fun setAlarm(
        context: Context,
        hours: Int,
        minute: Int,
        yer: Int?,
        month: Int?,
        dayOfYer: Int?,
        name: String,
        message: String,
        alarmId:Int,
    ) {

        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, YadinoBroadCastReceiver::class.java).let { intent ->
            intent.putExtra(ALARM_MESSAGE, message)
            intent.putExtra(ALARM_NAME, name)
            intent.putExtra(ALARM_ID, alarmId)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarmId,
            alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val t = calculateTime(yer, month, dayOfYer, hours, minute)
        if (t.timeInMillis < System.currentTimeMillis())
            return

        Timber.tag("time").d("calculateTime -> $t")
        Timber.tag("time").d("current time ->${Calendar.getInstance().time}")
        Timber.tag("time").d("current time yadino ->${t.time}")
        alarmManager.setAlarmClock(
            AlarmManager.AlarmClockInfo(t.timeInMillis, pendingIntent),
            pendingIntent
        )
    }

    override fun calculateTime(
        yer: Int?, month: Int?, dayOfYer: Int?, hours: Int,
        minute: Int,
    ): Calendar {
        val persianCalender = PersianDate()
        month?.let {
            persianCalender.setShMonth(it)
        }
        yer?.let {
            persianCalender.setShYear(it)
        }
        dayOfYer?.let {
            persianCalender.setShDay(it)
        }
        val month = persianCalender.grgMonth
        Timber.tag("time").d("calculateTimeMonth -> $month")
        val calendar: Calendar = Calendar.getInstance().apply {
            set(Calendar.MONTH, month.minus(1))
            set(Calendar.YEAR, persianCalender.grgYear)
            set(Calendar.DAY_OF_MONTH, persianCalender.grgDay)
            set(Calendar.HOUR_OF_DAY, hours)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar
    }
     override fun cancelAlarm(context: Context,idAlarm:Int?) {
         val intent = Intent(context, YadinoBroadCastReceiver::class.java)
         val pendingIntent = PendingIntent.getBroadcast(context, idAlarm?:0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
         val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
         alarmManager!!.cancel(pendingIntent)
     }
}