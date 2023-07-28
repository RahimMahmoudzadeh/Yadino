package com.rahim.utils.base.view

import android.content.Context
import com.rahim.data.alarm.AlarmManagement
import com.rahim.data.modle.Rotin.Routine
import com.rahim.ui.home.HomeViewModel
import com.rahim.ui.routine.RoutineViewModel

fun setAlarm(
    routine: Routine,
    alarmManagement: AlarmManagement,
    context: Context,
    addRoutineId: Int
) {
    alarmManagement.setAlarm(
        context,
        calculateHours(routine.timeHours.toString()),
        calculateMinute(routine.timeHours.toString()),
        routine.yerNumber,
        routine.monthNumber,
        routine.dayNumber,
        routine.name,
        routine.explanation ?: "",
        addRoutineId
    )
}

fun <T> removeRoutine(
    routine: Routine?,
    viewModel: T,
    alarmManagement: AlarmManagement,
    context: Context
) {
    routine?.let {
        if (viewModel is HomeViewModel)
            viewModel.deleteRoutine(routine)

        if (viewModel is RoutineViewModel)
            viewModel.deleteRoutine(routine)

        if (routine.id != 0) {
            alarmManagement.cancelAlarm(context, it.id)
        }
    }
}