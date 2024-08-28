package com.rahim.yadino.datetime_repository.mapper

import com.rahim.yadino.base.db.model.LocalTimeDateDto
import com.rahim.yadino.base.model.TimeDate

fun LocalTimeDateDto.toTimeDate(): TimeDate = TimeDate(
    dayNumber = dayNumber,
    haveTask = haveTask,
    isToday = isToday,
    nameDay = nameDay,
    yerNumber = yerNumber,
    monthNumber = monthNumber,
    monthName = monthName,
    isChecked = isChecked,
    versionNumber = versionNumber,
)

fun TimeDate.toLocalTimeDateDto(): LocalTimeDateDto = LocalTimeDateDto(
    dayNumber = dayNumber,
    haveTask = haveTask,
    isToday = isToday,
    nameDay = nameDay,
    yerNumber = yerNumber,
    monthNumber = monthNumber,
    monthName = monthName,
    isChecked = isChecked,
    versionNumber = versionNumber,
)