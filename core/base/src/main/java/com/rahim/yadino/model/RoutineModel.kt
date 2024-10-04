package com.rahim.yadino.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity("tbl_routine")
@Parcelize
data class RoutineModel(
    var name: String,
    var colorTask: Int?,
    var dayName: String,
    var dayNumber: Int?,
    var monthNumber: Int?,
    var yerNumber: Int?,
    var timeHours: String?,
    var isChecked: Boolean = false,
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var explanation: String? = null,
    var isSample:Boolean = false,
    var idAlarm:Long?=null,
    var timeInMillisecond:Long?=null
) : Parcelable
