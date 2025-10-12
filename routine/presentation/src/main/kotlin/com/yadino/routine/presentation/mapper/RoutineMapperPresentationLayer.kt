package com.yadino.routine.presentation.mapper

import com.rahim.yadino.core.timeDate.model.TimeDateModel
import com.yadino.routine.domain.model.RoutineModel
import com.yadino.routine.presentation.model.RoutineUiModel
import com.yadino.routine.presentation.model.TimeDateRoutinePresentationLayer

fun TimeDateModel.toTimeDateRoutinePresentationLayer(): TimeDateRoutinePresentationLayer = TimeDateRoutinePresentationLayer(
  dayNumber = this.dayNumber,
  haveTask = this.haveTask,
  isToday = this.isToday,
  nameDay = this.nameDay,
  yearNumber = this.yearNumber,
  monthName = this.monthName,
  monthNumber = this.monthNumber,
  isChecked = this.isChecked,
  versionNumber = this.versionNumber,
)

fun RoutineModel.toRoutinePresentationLayer(): RoutineUiModel =
  RoutineUiModel(
    name = this.name,
    colorTask = this.colorTask,
    dayName = this.dayName,
    dayNumber = this.dayNumber,
    monthNumber = this.monthNumber,
    yearNumber = this.yearNumber,
    timeHours = this.timeHours,
    isChecked = this.isChecked,
    id = this.id,
    explanation = this.explanation,
    isSample = this.isSample,
    idAlarm = this.idAlarm,
    timeInMillisecond = this.timeInMillisecond,
  )

fun RoutineUiModel.toRoutineDomainLayer(): RoutineModel =
  RoutineModel(
    name = this.name,
    colorTask = this.colorTask,
    dayName = this.dayName,
    dayNumber = this.dayNumber,
    monthNumber = this.monthNumber,
    yearNumber = this.yearNumber,
    timeHours = this.timeHours,
    isChecked = this.isChecked,
    id = this.id,
    explanation = this.explanation,
    isSample = this.isSample,
    idAlarm = this.idAlarm,
    timeInMillisecond = this.timeInMillisecond,
  )
