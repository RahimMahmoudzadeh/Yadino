package com.rahim.yadino.reminder

import com.rahim.yadino.reminder.state.ReminderState


interface ReminderScheduler {

    fun setReminder(
        reminderName: String,
        reminderId: Int,
        reminderTime: Long,
        reminderIdAlarm: Long
    ): ReminderState

    fun cancelReminder(id: String)
}