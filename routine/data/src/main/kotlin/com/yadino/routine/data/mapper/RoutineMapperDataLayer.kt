package com.yadino.routine.data.mapper

import com.rahim.yadino.db.dao.routine.model.RoutineEntity
import com.yadino.routine.domain.model.RoutineDomainLayer

fun RoutineDomainLayer.toRoutineEntity(): RoutineEntity = RoutineEntity(
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

fun RoutineEntity.toRoutineModelDomainLayer(): RoutineDomainLayer = RoutineDomainLayer(
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
