package com.rahim.yadino.routine.presentation.model

import kotlinx.serialization.Serializable

@Serializable
data class RoutineUiModel(
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
  val idAlarm: Int? = null,
  val timeInMillisecond: Long? = null,
)
