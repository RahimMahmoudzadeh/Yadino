package com.rahim.yadino.note.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NoteModel(
  val id: Int? = null,
  val name: String,
  val description: String,
  val isChecked: Boolean = false,
  val state: Int = 0,
  val dayName: String,
  val dayNumber: Int? = null,
  val monthNumber: Int? = null,
  val yearNumber: Int? = null,
  val isSample: Boolean = false,
  val timeInMileSecond: Long? = null,
) : Parcelable
