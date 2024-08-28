package com.rahim.yadino.base.db.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "tbl_note")
@Parcelize
data class LocalNoteDto(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var name: String,
    var description: String,
    var isChecked: Boolean = false,
    var state: Int = 0,
    var dayName: String,
    var dayNumber: Int?,
    var monthNumber: Int?,
    var yerNumber: Int?,
    var isSample: Boolean = false,
    var timeInMileSecond:Long?=null
) : Parcelable