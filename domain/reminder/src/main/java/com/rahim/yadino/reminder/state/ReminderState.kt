package com.rahim.yadino.reminder.state

sealed class ReminderState(val errorMessage: String? = null) {
    data object SetSuccessfully : ReminderState()
    data class PermissionsState(
        val reminderPermission: Boolean,
        val notificationPermission: Boolean
    ) : ReminderState()
    data class NotSet(val message: String) : ReminderState(message)
}