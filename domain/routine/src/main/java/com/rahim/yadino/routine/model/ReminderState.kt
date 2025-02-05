package com.rahim.yadino.routine.model

import com.rahim.yadino.enums.error.ErrorMessageCode

sealed class ReminderState(val errorMessage: ErrorMessageCode? = null) {
  data object SetSuccessfully : ReminderState()
  data class PermissionsState(
    val reminderPermission: Boolean,
    val notificationPermission: Boolean,
  ) : ReminderState()
  data class NotSet(val message: ErrorMessageCode) : ReminderState(message)
}
