package com.rahim.yadino.base.reminder

import com.rahim.yadino.enums.message.MessageCode

sealed class ReminderState(val errorMessage: MessageCode? = null) {
  data object SetSuccessfully : ReminderState()
  data class PermissionsState(
    val reminderPermission: Boolean,
    val notificationPermission: Boolean,
  ) : ReminderState()
  data class NotSet(val message: MessageCode) : ReminderState(message)
}
