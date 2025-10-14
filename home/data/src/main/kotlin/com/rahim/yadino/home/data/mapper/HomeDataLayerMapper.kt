package com.rahim.yadino.home.data.mapper

import com.rahim.home.domain.model.Routine
import com.rahim.yadino.db.routine.model.RoutineEntity

fun Routine.toRoutineEntity(): RoutineEntity = RoutineEntity(
  name = name,
  colorTask = colorTask,
  dayName = dayName,
  dayNumber = dayNumber,
  monthNumber = monthNumber,
  yearNumber = yearNumber,
  timeHours = timeHours,
  isChecked = isChecked,
  id = id,
  explanation = explanation,
  isSample = isSample,
  idAlarm = idAlarm,
  timeInMillisecond = timeInMillisecond,
)

fun RoutineEntity.toRoutine(): Routine = Routine(
  name = name,
  colorTask = colorTask,
  dayName = dayName,
  dayNumber = dayNumber,
  monthNumber = monthNumber,
  yearNumber = yearNumber,
  timeHours = timeHours,
  isChecked = isChecked,
  id = id,
  explanation = explanation,
  isSample = isSample,
  idAlarm = idAlarm,
  timeInMillisecond = timeInMillisecond,
)
