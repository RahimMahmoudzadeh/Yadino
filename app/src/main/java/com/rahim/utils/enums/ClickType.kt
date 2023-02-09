package com.rahim.utils.enums

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

enum class ClickType(val type: String) {
    SET_TIME("setTime"),
    ADD_ROUTINE("addRoutine"),
    OK("ok")
}