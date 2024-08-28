package com.rahim.yadino.routine_repository.mapper

import com.rahim.yadino.base.db.model.LocalRoutineDto
import com.rahim.yadino.routine.modle.Routine

fun LocalRoutineDto.toRoutine(): Routine = Routine(
    id = id,
    name = name,
    timeInMillisecond = timeInMillisecond,
    dayName = dayName,
    dayNumber = dayNumber,
    monthNumber = monthNumber,
    yerNumber = yerNumber,
    timeHours = timeHours,
    idAlarm = idAlarm,
    isSample = isSample,
    isChecked = isChecked,
    explanation = explanation,
    colorTask = colorTask,

)

fun Routine.toLocalRoutineDto(): LocalRoutineDto = LocalRoutineDto(
    id = id,
    name = name,
    timeInMillisecond = timeInMillisecond,
    dayName = dayName,
    dayNumber = dayNumber,
    monthNumber = monthNumber,
    yerNumber = yerNumber,
    timeHours = timeHours,
    idAlarm = idAlarm,
    isSample = isSample,
    isChecked = isChecked,
    explanation = explanation,
    colorTask = colorTask,
)