package com.rahim.yadino.db.routine.model

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
  val idAlarm: Int? = null,
  val timeInMillisecond: Long? = null,
)
