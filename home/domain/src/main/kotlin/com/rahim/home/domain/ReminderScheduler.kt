package com.rahim.home.domain


interface ReminderScheduler {

  fun setReminder(reminderName: String, reminderId: Int, reminderTime: Long, reminderIdAlarm: Long): ReminderState

  suspend fun cancelReminder(id: Long)
}
