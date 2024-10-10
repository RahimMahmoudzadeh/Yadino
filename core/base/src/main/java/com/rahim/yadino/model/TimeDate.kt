package com.rahim.yadino.model

import androidx.room.Entity

@Entity(tableName = "tbl_timeDate", primaryKeys = ["dayNumber","yerNumber","monthNumber"])
data class TimeDate(
    val dayNumber: Int,
    val haveTask: Boolean,
    val isToday: Boolean = false,
    val nameDay: String,
    val yerNumber: Int,
    val monthNumber: Int,
    val isChecked: Boolean,
    val monthName: String? = null,
    val versionNumber: Long? = null,
)
