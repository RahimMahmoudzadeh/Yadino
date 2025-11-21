package com.rahim.data.reminder

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.rahim.yadino.base.reminder.ReminderScheduler
import com.rahim.yadino.base.reminder.ReminderState
import com.rahim.yadino.Constants
import com.rahim.yadino.enums.error.ErrorMessageCode
import kotlinx.coroutines.delay
import timber.log.Timber

class ReminderSchedulerImpl(
    private val alarmManager: AlarmManager,
    private val context: Context,
) : ReminderScheduler {

  override fun setReminder(reminderName: String, reminderId: Int, reminderTime: Long, reminderIdAlarm: Int): ReminderState {
    if (reminderTime < System.currentTimeMillis()) return ReminderState.NotSet(ErrorMessageCode.ERROR_TIME_PASSED)

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      checkPermissionAfterApiLevel33(reminderName, reminderTime, reminderIdAlarm)
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
      checkPermissionAfterApiLevel31(reminderName, reminderTime, reminderIdAlarm)
    } else {
      setAlarm(
        reminderName,
        reminderTime,
        reminderIdAlarm,
      ).let { ReminderState.SetSuccessfully }
    }
  }

  @RequiresApi(Build.VERSION_CODES.S)
  private fun checkPermissionAfterApiLevel31(reminderName: String, reminderTime: Long, reminderIdAlarm: Int): ReminderState {
    return if (alarmManager.canScheduleExactAlarms()) {
      setAlarm(
        reminderName,
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
  private fun checkPermissionAfterApiLevel33(reminderName: String, reminderTime: Long, reminderIdAlarm: Int): ReminderState {
    return when {
      alarmManager.canScheduleExactAlarms() && areNotificationsEnabled(context) -> {
        setAlarm(
          reminderName,
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

  private fun setAlarm(reminderName: String, reminderTime: Long, reminderAlarmId: Int) {
    val alarmIntent = Intent(context, YadinoBroadCastReceiver::class.java).apply {
      putExtra(Constants.KEY_LAUNCH_NAME, reminderName)
      putExtra(Constants.KEY_REMINDER_ALARM_ID, reminderAlarmId)
    }

    val pendingIntent = PendingIntent.getBroadcast(
      context,
      reminderAlarmId,
      alarmIntent,
      PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
    )
    alarmManager.setAlarmClock(
      AlarmManager.AlarmClockInfo(reminderTime, pendingIntent),
      pendingIntent,
    )
  }

  override suspend fun cancelReminder(reminderAlarmId: Int) {
    val intent = Intent(context, YadinoBroadCastReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
      context,
      reminderAlarmId,
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
