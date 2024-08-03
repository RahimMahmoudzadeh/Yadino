package com.rahim.yadino.routine.modle.Routine

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Routine(
    var name: String,
    var colorTask: Int?,
    var dayName: String,
    var dayNumber: Int?,
    var monthNumber: Int?,
    var yerNumber: Int?,
    var timeHours: String?,
    var isChecked: Boolean = false,
    var id: Int? = null,
    var explanation: String? = null,
    var isSample:Boolean = false,
    var idAlarm:Long?=null,
    var timeInMillisecond:Long?=null
) : Parcelable