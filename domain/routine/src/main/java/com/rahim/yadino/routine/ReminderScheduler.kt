package com.rahim.yadino.routine

import com.rahim.yadino.routine.modle.ReminderState


interface ReminderScheduler {

    fun setReminder(
        reminderName: String,
        reminderId: Int,
        reminderTime: Long,
        reminderIdAlarm: Long
    ): ReminderState

    fun cancelReminder(id: String)
}