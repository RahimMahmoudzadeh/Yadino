package com.rahim.yadino.routine.modle.Rotin

import android.os.Parcelable


@Parcelize
data class Routine(
    var name: String,
    var colorTask: Int?,
    var dayName: String,
    val dayNumber: Int?,
    val monthNumber: Int?,
    val yerNumber: Int?,
    var timeHours: String?,
    var isChecked: Boolean = false,
    var id: Int? = null,
    var explanation: String? = null,
    var isSample:Boolean = false,
    var idAlarm:Long?=null,
    var timeInMillisecond:Long?=null
) : Parcelable