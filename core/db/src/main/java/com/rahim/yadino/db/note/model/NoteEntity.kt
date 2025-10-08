package com.rahim.yadino.db.note.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
@Entity(tableName = "tbl_note")
data class NoteEntity(
  @PrimaryKey(autoGenerate = true)
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
)
