package com.rahim.data.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.rahim.data.broadcast.YadinoBroadCastReceiver
import com.rahim.data.date.CalculateDate
import com.rahim.utils.Constants.ALARM_MESSAGE
import com.rahim.utils.Constants.ALARM_NAME
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import saman.zamani.persiandate.PersianDate
import timber.log.Timber
import java.util.Calendar

class AlarmManagement : CalculateDate {
    fun setAlarm(
        context: Context,
        hours: Int,
        minute: Int,
        yer: Int?,
        month: Int?,
        dayOfYer: Int?,
        name: String,
        message: String
    ) {

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
        val t = calculateTime(yer, month, dayOfYer, hours, minute)
        Timber.tag("time").d(t.toString())
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            t.timeInMillis,
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
        Timber.tag("time")
            .d("day in yer-> ${persianCalender.getDayInYear(month ?: 1, dayOfYer ?: 1)}")
        val calendar: Calendar = Calendar.getInstance().apply {
            set(Calendar.MONTH, persianCalender.grgMonth)
            set(Calendar.YEAR, persianCalender.grgYear)
            set(Calendar.DAY_OF_MONTH, persianCalender.grgDay)
            set(Calendar.HOUR_OF_DAY, hours)
            set(Calendar.HOUR, hours)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar
    }

}