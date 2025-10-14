package com.rahim.yadino.home.presentation.mapper

import com.rahim.home.domain.model.CurrentDate
import com.rahim.home.domain.model.Routine

fun com.rahim.yadino.home.presentation.model.RoutineHomeModel.toRoutineHomeDomainLayer(): Routine =
  Routine(
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


fun Routine.toRoutineHomePresentationLayer(): com.rahim.yadino.home.presentation.model.RoutineHomeModel =
  com.rahim.yadino.home.presentation.model.RoutineHomeModel(
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

fun CurrentDate.toCurrentDatePresentationLayer(): com.rahim.yadino.home.presentation.model.CurrentDateModel = com.rahim.yadino.home.presentation.model.CurrentDateModel(date = this.date)
