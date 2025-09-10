package com.rahim.yadino.core.timeDate.mapper

import com.rahim.yadino.core.timeDate.model.TimeDateModel
import com.rahim.yadino.db.dao.dateTime.model.TimeDateEntity

fun TimeDateModel.toTimeDateEntity(): TimeDateEntity = TimeDateEntity(
  isChecked = isChecked,
  dayNumber = dayNumber, monthNumber = monthNumber, yearNumber = yearNumber, monthName = monthName, versionNumber = versionNumber,
  nameDay = nameDay, haveTask = haveTask, isToday = isToday,
)

fun TimeDateEntity.toTimeDate(): TimeDateModel = TimeDateModel(
  isChecked = isChecked,
  dayNumber = dayNumber, monthNumber = monthNumber, yearNumber = yearNumber, monthName = monthName, versionNumber = versionNumber,
  nameDay = nameDay, haveTask = haveTask, isToday = isToday,
)
