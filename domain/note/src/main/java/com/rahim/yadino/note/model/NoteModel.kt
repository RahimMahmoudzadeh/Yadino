package com.rahim.yadino.note.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NoteModel(
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