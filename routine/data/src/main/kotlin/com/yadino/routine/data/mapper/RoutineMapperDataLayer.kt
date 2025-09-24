package com.yadino.routine.data.mapper

import com.rahim.yadino.db.dao.routine.model.RoutineEntity
import com.yadino.routine.domain.model.RoutineModelDomainLayer

fun RoutineModelDomainLayer.toRoutineEntity(): RoutineEntity = RoutineEntity(
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

fun RoutineEntity.toRoutineModelDomainLayer(): RoutineModelDomainLayer = RoutineModelDomainLayer(
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
