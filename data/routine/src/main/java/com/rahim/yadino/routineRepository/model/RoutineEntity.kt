package com.rahim.yadino.routineRepository.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("tbl_routine")
data class RoutineEntity(
  val name: String,
  val colorTask: Int?,
  val dayName: String,
  val dayNumber: Int?,
  val monthNumber: Int?,
  val yearNumber: Int?,
  val timeHours: String?,
  val isChecked: Boolean = false,
  @PrimaryKey(autoGenerate = true)
  val id: Int? = null,
  val explanation: String? = null,
  val isSample: Boolean = false,
  val idAlarm: Long? = null,
  val timeInMillisecond: Long? = null,
)
