package com.rahim.yadino.dateTime_local.dto

import androidx.room.Entity


@Entity(tableName = "tbl_timeDate", primaryKeys = ["dayNumber","yerNumber","monthNumber"])
data class LocalTimeDateDto(
    val dayNumber: Int,
    val haveTask: Boolean,
    var isToday: Boolean = false,
    val nameDay: String,
    val yerNumber: Int,
    val monthNumber: Int,
    var isChecked: Boolean,
    var monthName: String? = null,
    var versionNumber: Long? = null,
)