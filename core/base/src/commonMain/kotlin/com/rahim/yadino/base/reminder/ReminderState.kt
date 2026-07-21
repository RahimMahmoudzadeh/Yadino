package com.rahim.yadino.base.reminder

import com.rahim.yadino.enums.message.error.ErrorMessage

sealed class ReminderState(val errorMessage: ErrorMessage? = null) {
  data object SetSuccessfully : ReminderState()
  data class PermissionsState(
    val reminderPermission: Boolean,
    val notificationPermission: Boolean,
  ) : ReminderState()
  data class NotSet(val message: ErrorMessage) : ReminderState(message)
}
