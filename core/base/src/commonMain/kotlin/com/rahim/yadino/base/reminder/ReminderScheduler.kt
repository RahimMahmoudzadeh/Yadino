package com.rahim.yadino.base.reminder

interface ReminderScheduler {

  fun setReminder(reminderName: String, reminderExplanation: String, reminderId: Int, reminderTime: Long, reminderIdAlarm: Int): ReminderState

  suspend fun cancelReminder(reminderAlarmId: Int)
}
