package com.rahim.data.modle.Rotin

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "tbl_routine")
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
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var explanation: String? = null,
    var isSample:Boolean = false,
    var idAlarm:Long?=null
) : Parcelable