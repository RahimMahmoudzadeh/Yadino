package com.rahim.yadino.routine.reminder

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.rahim.yadino.base.Constants.ACTION_CANCEL_NOTIFICATION
import com.rahim.yadino.base.Constants.KEY_LAUNCH_ID
import com.rahim.yadino.base.Constants.KEY_LAUNCH_NAME
import com.rahim.yadino.base.enums.error.ErrorMessageCode
import com.rahim.yadino.routine.ReminderScheduler
import com.rahim.yadino.routine.modle.ReminderState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import timber.log.Timber
import javax.inject.Inject

class ReminderSchedulerImpl @Inject constructor(
  private val alarmManager: AlarmManager,
  private val context: Context,
) : ReminderScheduler {

  override fun setReminder(
    reminderName: String,
    reminderId: Int,
    reminderTime: Long,
    reminderIdAlarm: Long,
  ): ReminderState {
    if (reminderTime < System.currentTimeMillis()) return ReminderState.NotSet(ErrorMessageCode.ERROR_TIME_PASSED)

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      checkPermissionAfterApiLevel33(reminderName, reminderId, reminderTime, reminderIdAlarm)
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
      checkPermissionAfterApiLevel31(reminderName, reminderId, reminderTime, reminderIdAlarm)
    } else {
      setAlarm(
        reminderName,
        reminderId,
        reminderTime,
        reminderIdAlarm,
      ).let { ReminderState.SetSuccessfully }
    }
  }

  private fun checkPermissionAfterApiLevel31(reminderName: String, reminderId: Int, reminderTime: Long, reminderIdAlarm: Long): ReminderState {
    return if (alarmManager.canScheduleExactAlarms()) {
      setAlarm(
        reminderName,
        reminderId,
        reminderTime,
        reminderIdAlarm,
      )
      ReminderState.SetSuccessfully
    } else {
      ReminderState.PermissionsState(
        reminderPermission = false,
        notificationPermission = true,
      )
    }
  }

  @SuppressLint("NewApi")
  private fun checkPermissionAfterApiLevel33(
    reminderName: String,
    reminderId: Int,
    reminderTime: Long,
    reminderIdAlarm: Long,
  ): ReminderState {
    return when {
      alarmManager.canScheduleExactAlarms() && areNotificationsEnabled(context) -> {
        setAlarm(
          reminderName,
          reminderId,
          reminderTime,
          reminderIdAlarm,
        ).let { ReminderState.SetSuccessfully }
      }

      alarmManager.canScheduleExactAlarms() && !areNotificationsEnabled(context) -> {
        ReminderState.PermissionsState(
          reminderPermission = true,
          notificationPermission = false,
        )
      }

      !alarmManager.canScheduleExactAlarms() && areNotificationsEnabled(context) -> {
        ReminderState.PermissionsState(
          reminderPermission = false,
          notificationPermission = true,
        )
      }

      else -> {
        ReminderState.PermissionsState(
          reminderPermission = false,
          notificationPermission = false,
        )
      }
    }
  }


  private fun setAlarm(
    reminderName: String,
    reminderId: Int,
    reminderTime: Long,
    reminderIdAlarm: Long,
  ) {
    val alarmIntent = Intent(context, YadinoBroadCastReceiver::class.java).apply {
      putExtra(KEY_LAUNCH_NAME, reminderName)
      putExtra(KEY_LAUNCH_ID, reminderId)
    }
    Timber.tag("intentTitle").d("AndroidReminderScheduler setAlarm-> ${reminderName}")
    val pendingIntent = PendingIntent.getBroadcast(
      context,
      reminderIdAlarm.toInt(),
      alarmIntent,
      PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
    )
    alarmManager.setAlarmClock(
      AlarmManager.AlarmClockInfo(reminderTime, pendingIntent),
      pendingIntent,
    )
  }

  override suspend fun cancelReminder(id: Long) {
    val intent = Intent(context, YadinoBroadCastReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
      context,
      id.toInt(),
      intent,
      PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
    )
    alarmManager.cancel(
      pendingIntent,
    )
    delay(100)
  }

  @SuppressLint("InlinedApi")
  private fun areNotificationsEnabled(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
      context,
      Manifest.permission.POST_NOTIFICATIONS,
    ) == PackageManager.PERMISSION_GRANTED
  }
}
