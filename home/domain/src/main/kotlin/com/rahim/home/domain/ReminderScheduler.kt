package com.rahim.home.domain

import com.rahim.home.domain.model.ReminderState


interface ReminderScheduler {

  fun setReminder(reminderName: String, reminderId: Int, reminderTime: Long, reminderIdAlarm: Long): ReminderState

  suspend fun cancelReminder(id: Long)
}
