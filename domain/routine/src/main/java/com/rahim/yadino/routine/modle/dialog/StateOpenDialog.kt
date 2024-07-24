package com.rahim.yadino.routine.modle.dialog

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StateOpenDialog(var isOpen: Boolean, val destination: String) : Parcelable
