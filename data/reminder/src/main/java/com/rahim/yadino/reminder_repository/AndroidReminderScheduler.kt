package com.rahim.yadino.reminder_repository

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
import com.rahim.yadino.base.Constants.ACTION_SEND_NOTIFICATION
import com.rahim.yadino.base.Constants.KEY_LAUNCH_ID
import com.rahim.yadino.base.Constants.KEY_LAUNCH_NAME
import com.rahim.yadino.base.broadcast.YadinoBroadCastReceiver
import com.rahim.yadino.base.enums.error.ErrorMessageCode
import com.rahim.yadino.reminder.ReminderScheduler
import com.rahim.yadino.reminder.state.ReminderState
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AndroidReminderScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) : ReminderScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun setReminder(
        reminderName: String,
        reminderId: Int,
        reminderTime: Long,
        reminderIdAlarm: Long
    ): ReminderState {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return when {
                alarmManager.canScheduleExactAlarms() && areNotificationsEnabled(context) -> {
                    return setAlarm(
                        reminderName,
                        reminderId,
                        reminderTime,
                        reminderIdAlarm
                    ).let { errorMessage ->
                        if (errorMessage == null) {
                            ReminderState.SetSuccessfully
                        } else {
                            ReminderState.NotSet(errorMessage)
                        }
                    }
                }

                alarmManager.canScheduleExactAlarms() && !areNotificationsEnabled(context) -> {
                    ReminderState.PermissionsState(
                        reminderPermission = true,
                        notificationPermission = false
                    )
                }

                !alarmManager.canScheduleExactAlarms() && areNotificationsEnabled(context) -> {
                    ReminderState.PermissionsState(
                        reminderPermission = false,
                        notificationPermission = true
                    )
                }

                !alarmManager.canScheduleExactAlarms() && !areNotificationsEnabled(context) -> {
                    ReminderState.PermissionsState(
                        reminderPermission = false,
                        notificationPermission = false
                    )
                }

                else -> {
                    ReminderState.PermissionsState(
                        reminderPermission = false,
                        notificationPermission = false
                    )
                }
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return if (alarmManager.canScheduleExactAlarms()) {
                return setAlarm(
                    reminderName,
                    reminderId,
                    reminderTime,
                    reminderIdAlarm
                ).let { errorMessage ->
                    if (errorMessage == null) {
                        ReminderState.SetSuccessfully
                    } else {
                        ReminderState.NotSet(errorMessage)
                    }
                }
            } else {
                ReminderState.PermissionsState(
                    reminderPermission = false,
                    notificationPermission = true
                )
            }
        } else {
            return setAlarm(
                reminderName,
                reminderId,
                reminderTime,
                reminderIdAlarm
            ).let { errorMessage ->
                if (errorMessage == null) {
                    ReminderState.SetSuccessfully
                } else {
                    ReminderState.NotSet(errorMessage)
                }
            }
        }
    }

    override fun cancelReminder(id: String) {
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id.hashCode(),
            Intent(ACTION_CANCEL_NOTIFICATION),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(
            pendingIntent
        )
    }

    private fun setAlarm(
        reminderName: String,
        reminderId: Int,
        reminderTime: Long,
        reminderIdAlarm: Long
    ): ErrorMessageCode? {
        if (reminderTime < System.currentTimeMillis()) return ErrorMessageCode.ERROR_TIME_PASSED
        val alarmIntent = Intent(context, YadinoBroadCastReceiver::class.java).apply {
            putExtra(KEY_LAUNCH_NAME, reminderName)
            putExtra(KEY_LAUNCH_ID, reminderId)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            reminderIdAlarm.toInt(),
            alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.setAlarmClock(
            AlarmManager.AlarmClockInfo(reminderTime, pendingIntent),
            pendingIntent
        )
        return null
    }

    @SuppressLint("InlinedApi")
    private fun areNotificationsEnabled(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }
}
