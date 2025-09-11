package com.rahim.home.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class RoutineHomeDomainLayer(
  val name: String,
  val colorTask: Int?,
  val dayName: String,
  val dayNumber: Int?,
  val monthNumber: Int?,
  val yearNumber: Int?,
  val timeHours: String?,
  val isChecked: Boolean = false,
  val id: Int? = null,
  val explanation: String? = null,
  val isSample: Boolean = false,
  val idAlarm: Long? = null,
  val timeInMillisecond: Long? = null,
)
