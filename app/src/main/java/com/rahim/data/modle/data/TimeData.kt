package com.rahim.data.modle.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_timeData")
data class TimeData(
    val dayNumber: Int,
    val haveTask: Boolean?,
    var isToday: Boolean = false,
    val nameDay: String?,
    val yerNumber: Int?,
    val monthNumber: Int?,
    var isChecked: Boolean?,
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
)