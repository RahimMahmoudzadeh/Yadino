package com.rahim.yadino.home.data.mapper

import com.rahim.home.domain.model.RoutineModel
import com.rahim.home.domain.model.TimeDateDomainLayerHome
import com.rahim.yadino.db.dao.dateTime.model.TimeDateEntity
import com.rahim.yadino.db.dao.routine.model.RoutineEntity

fun RoutineModel.toRoutineEntity(): RoutineEntity = RoutineEntity(
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

fun RoutineEntity.toRoutineModel(): RoutineModel = RoutineModel(
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

fun TimeDateDomainLayerHome.toTimeDateEntity(): TimeDateEntity = TimeDateEntity(
  isChecked = isChecked,
  dayNumber = dayNumber, monthNumber = monthNumber, yearNumber = yearNumber, monthName = monthName, versionNumber = versionNumber,
  nameDay = nameDay, haveTask = haveTask, isToday = isToday,
)

fun TimeDateEntity.toTimeDate(): TimeDateDomainLayerHome = TimeDateDomainLayerHome(
  isChecked = isChecked,
  dayNumber = dayNumber, monthNumber = monthNumber, yearNumber = yearNumber, monthName = monthName, versionNumber = versionNumber,
  nameDay = nameDay, haveTask = haveTask, isToday = isToday,
)
