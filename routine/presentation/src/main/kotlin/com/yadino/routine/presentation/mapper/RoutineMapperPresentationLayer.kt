package com.yadino.routine.presentation.mapper

import com.rahim.yadino.core.timeDate.model.TimeDateModel
import com.yadino.routine.presentation.model.TimeDateRoutinePresentationLayer

fun TimeDateModel.toTimeDateRoutinePresentationLayer(): TimeDateRoutinePresentationLayer =
  TimeDateRoutinePresentationLayer(
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
