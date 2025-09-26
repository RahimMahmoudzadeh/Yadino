package com.yadino.routine.presentation.mapper

import com.rahim.yadino.core.timeDate.model.TimeDateModel
import com.yadino.routine.domain.model.RoutineDomainLayer
import com.yadino.routine.presentation.model.RoutinePresentationLayer
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

fun RoutineDomainLayer.toRoutinePresentationLayer(): RoutinePresentationLayer =
  RoutinePresentationLayer(
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

fun RoutinePresentationLayer.toRoutineDomainLayer(): RoutineDomainLayer =
  RoutineDomainLayer(
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
