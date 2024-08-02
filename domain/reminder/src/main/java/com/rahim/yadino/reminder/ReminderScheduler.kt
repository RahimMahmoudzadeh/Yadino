package com.rahim.yadino.reminder

import com.rahim.yadino.reminder.state.ReminderState


interface ReminderScheduler {

    fun setReminder(
        reminderName: String,
        reminderId: Long,
        reminderTime: Long,
        reminderIdAlarm: Int
    ): ReminderState

    fun cancelReminder(id: String)
}