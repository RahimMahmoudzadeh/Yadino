package com.rahim.yadino.base.dateTime.mapper

import com.rahim.yadino.base.dateTime.modal.TimeDate
import com.rahim.yadino.db.dao.dateTime.model.TimeDateEntity

fun TimeDate.toTimeDateEntity(): TimeDateEntity = TimeDateEntity(
  isChecked = isChecked,
  dayNumber = dayNumber, monthNumber = monthNumber, yearNumber = yearNumber, monthName = monthName, versionNumber = versionNumber,
  nameDay = nameDay, haveTask = haveTask, isToday = isToday,
)

fun TimeDateEntity.toTimeDate(): TimeDate = TimeDate(
  isChecked = isChecked,
  dayNumber = dayNumber, monthNumber = monthNumber, yearNumber = yearNumber, monthName = monthName, versionNumber = versionNumber,
  nameDay = nameDay, haveTask = haveTask, isToday = isToday,
)
