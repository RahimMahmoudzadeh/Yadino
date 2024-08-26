package com.rahim.data.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.rahim.data.broadcast.YadinoBroadCastReceiver
import com.rahim.data.model.routine.RoutineModel
import com.rahim.utils.Constants.ROUTINE
import kotlinx.coroutines.delay

class AlarmManagement {
  suspend fun updateAlarm(
    context: Context,
    routineModel: RoutineModel,
  ) {
    if ((routineModel.timeInMillisecond ?: 0) < System.currentTimeMillis()) return

    cancelAlarm(context, routineModel.idAlarm)
    val alarmManager =
      context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val alarmIntent = Intent(context, YadinoBroadCastReceiver::class.java).let { intent ->
      intent.putExtra(ROUTINE, routineModel)
    }

    val pendingIntent = PendingIntent.getBroadcast(
      context,
      routineModel.idAlarm?.toInt() ?: 0,
      alarmIntent,
      PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
    )

    alarmManager.setAlarmClock(
      AlarmManager.AlarmClockInfo(routineModel.timeInMillisecond ?: 0, pendingIntent),
      pendingIntent,
    )
  }

  fun setAlarm(
    context: Context,
    routineModel: RoutineModel,
  ) {
    if ((routineModel.timeInMillisecond ?: 0) < System.currentTimeMillis()) return
    val alarmManager =
      context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    val alarmIntent = Intent(context, YadinoBroadCastReceiver::class.java).let { intent ->
      intent.putExtra(ROUTINE, routineModel)
    }

    val pendingIntent = PendingIntent.getBroadcast(
      context,
      routineModel.idAlarm?.toInt() ?: 0,
      alarmIntent,
      PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
    )
    alarmManager.setAlarmClock(
      AlarmManager.AlarmClockInfo(routineModel.timeInMillisecond ?: 0, pendingIntent),
      pendingIntent,
    )
  }

  suspend fun cancelAlarm(context: Context, idAlarm: Long?) {
    val intent = Intent(context, YadinoBroadCastReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
      context,
      idAlarm?.toInt() ?: 0,
      intent,
      PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
    )
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
    alarmManager?.cancel(pendingIntent)
    delay(100)
  }
}
