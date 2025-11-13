package com.yadino.routine.data.mapper

import com.rahim.yadino.db.routine.model.RoutineEntity
import com.rahim.yadino.routine.domain.model.Routine

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

fun RoutineEntity.toRoutineModelDomainLayer(): Routine = Routine(
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
