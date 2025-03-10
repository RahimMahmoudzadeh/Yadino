package com.rahim.yadino.dateTime.modal

data class TimeDate(
  val dayNumber: Int,
  val haveTask: Boolean,
  val isToday: Boolean = false,
  val nameDay: String,
  val yearNumber: Int,
  val monthNumber: Int,
  val isChecked: Boolean,
  val monthName: String? = null,
  val versionNumber: Long? = null,
)
