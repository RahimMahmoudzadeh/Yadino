package com.rahim.yadino.reminder.state

import com.rahim.yadino.base.enums.error.ErrorMessageCode

sealed class ReminderState(val errorMessage: ErrorMessageCode? = null) {
    data object SetSuccessfully : ReminderState()
    data class PermissionsState(
        val reminderPermission: Boolean,
        val notificationPermission: Boolean
    ) : ReminderState()
    data class NotSet(val message: ErrorMessageCode) : ReminderState(message)
}