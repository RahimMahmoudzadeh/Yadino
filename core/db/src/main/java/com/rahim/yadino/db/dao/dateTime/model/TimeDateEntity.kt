package com.rahim.yadino.db.dao.dateTime.model

import androidx.room.Entity

@Entity(tableName = "tbl_timeDate", primaryKeys = ["dayNumber", "yearNumber", "monthNumber"])
data class TimeDateEntity(
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
