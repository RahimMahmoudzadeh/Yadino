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

class ReminderSchedulerImpl(
  private val alarmManager: AlarmManager,
  private val context: Context,
) : ReminderScheduler {

  override fun setReminder(reminderName: String, reminderExplanation: String, reminderId: Int, reminderTime: Long, reminderIdAlarm: Int): ReminderState {
    if (reminderTime < System.currentTimeMillis()) return ReminderState.NotSet(ErrorMessageCode.ERROR_TIME_PASSED)

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      checkPermissionAfterApiLevel33(reminderName = reminderName, reminderExplanation = reminderExplanation, reminderTime = reminderTime, reminderIdAlarm = reminderIdAlarm)
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
      checkPermissionAfterApiLevel31(reminderName = reminderName, reminderExplanation = reminderExplanation, reminderTime = reminderTime, reminderIdAlarm = reminderIdAlarm)
    } else {
      setAlarm(
        reminderName = reminderName,
        reminderExplanation = reminderExplanation,
        reminderTime = reminderTime,
        reminderAlarmId = reminderIdAlarm,
      ).let { ReminderState.SetSuccessfully }
    }
  }

  @RequiresApi(Build.VERSION_CODES.S)
  private fun checkPermissionAfterApiLevel31(reminderName: String, reminderExplanation: String, reminderTime: Long, reminderIdAlarm: Int): ReminderState {
    return if (alarmManager.canScheduleExactAlarms()) {
      setAlarm(
        reminderName = reminderName,
        reminderExplanation = reminderExplanation,
        reminderTime = reminderTime,
        reminderAlarmId = reminderIdAlarm,
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
  private fun checkPermissionAfterApiLevel33(reminderName: String, reminderExplanation: String, reminderTime: Long, reminderIdAlarm: Int): ReminderState {
    return when {
      alarmManager.canScheduleExactAlarms() && areNotificationsEnabled(context) -> {
        setAlarm(
          reminderName = reminderName,
          reminderExplanation = reminderExplanation,
          reminderTime = reminderTime,
          reminderAlarmId = reminderIdAlarm,
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

  private fun setAlarm(reminderName: String, reminderExplanation: String, reminderTime: Long, reminderAlarmId: Int) {
    val alarmIntent = Intent(context, YadinoBroadCastReceiver::class.java).apply {
      putExtra(Constants.REMINDER_NAME, reminderName)
      putExtra(Constants.REMINDER_EXPLANATION_NAME, reminderExplanation)
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
