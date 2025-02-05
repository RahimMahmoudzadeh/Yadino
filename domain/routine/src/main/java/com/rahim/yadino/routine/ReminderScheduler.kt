package com.rahim.yadino.routine

import com.rahim.yadino.routine.model.ReminderState

interface ReminderScheduler {

  fun setReminder(reminderName: String, reminderId: Int, reminderTime: Long, reminderIdAlarm: Long): ReminderState

  suspend fun cancelReminder(id: Long)
}
