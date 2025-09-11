package com.rahim.yadino.home.presentation.mapper

import com.rahim.home.domain.model.CurrentDateDomainLayer
import com.rahim.home.domain.model.RoutineHomeDomainLayer
import com.rahim.yadino.home.presentation.model.CurrentDatePresentationLayer
import com.rahim.yadino.home.presentation.model.RoutineHomePresentationLayer

fun RoutineHomePresentationLayer.toRoutineHomeDomainLayer(): RoutineHomeDomainLayer =
  RoutineHomeDomainLayer(
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


fun RoutineHomeDomainLayer.toRoutineHomePresentationLayer(): RoutineHomePresentationLayer =
  RoutineHomePresentationLayer(
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

fun CurrentDateDomainLayer.toCurrentDatePresentationLayer(): CurrentDatePresentationLayer = CurrentDatePresentationLayer(date = this.date)
