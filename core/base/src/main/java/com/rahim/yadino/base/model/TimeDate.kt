package com.rahim.yadino.base.model


data class TimeDate(
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