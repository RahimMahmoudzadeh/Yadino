package com.rahim.yadino.home.presentation.mapper

import com.rahim.home.domain.model.CurrentDate
import com.rahim.home.domain.model.Routine
import com.rahim.yadino.home.presentation.model.RoutineUiModel

fun RoutineUiModel.toRoutine(): Routine =
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


fun Routine.toRoutineUiModel(): RoutineUiModel =
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

fun CurrentDate.toCurrentDatePresentationLayer(): com.rahim.yadino.home.presentation.model.CurrentDateUiModel = com.rahim.yadino.home.presentation.model.CurrentDateUiModel(date = this.date)
