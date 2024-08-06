package com.rahim.data.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.rahim.yadino.base.Constants.ROUTINE
import kotlinx.coroutines.delay

//class AlarmManagement {
//    suspend fun updateAlarm(
//        context: Context,
//        routine: com.rahim.yadino.routine.modle.Rotin.Routine,
//    ) {
//        if ((routine.timeInMillisecond ?: 0) < System.currentTimeMillis()) return
//
//        cancelAlarm(context, routine.idAlarm)
//        val alarmManager =
//            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val alarmIntent = Intent(context, com.rahim.yadino.reminder_repository.YadinoBroadCastReceiver::class.java).let { intent ->
//            intent.putExtra(ROUTINE, routine)
//        }
//
//        val pendingIntent = PendingIntent.getBroadcast(
//            context,
//            routine.idAlarm?.toInt() ?: 0,
//            alarmIntent,
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )
//
//
//        alarmManager.setAlarmClock(
//            AlarmManager.AlarmClockInfo(routine.timeInMillisecond ?: 0, pendingIntent),
//            pendingIntent
//        )
//    }
//
//    fun setAlarm(
//        context: Context,
//        routine: com.rahim.yadino.routine.modle.Rotin.Routine,
//    ) {
//
//    }
//
//    suspend fun cancelAlarm(context: Context, idAlarm: Long?) {
//        val intent = Intent(context, com.rahim.yadino.reminder_repository.YadinoBroadCastReceiver::class.java)
//        val pendingIntent = PendingIntent.getBroadcast(
//            context,
//            idAlarm?.toInt() ?: 0,
//            intent,
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )
//        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
//        alarmManager?.cancel(pendingIntent)
//        delay(100)
//    }
//}