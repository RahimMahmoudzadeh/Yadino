package com.rahim.yadino.note.presentation.model

import kotlinx.serialization.Serializable

@Serializable
data class NoteUiModel(
  val id: Int,
  val name: String,
  val description: String,
  val isChecked: Boolean = false,
  val state: PriorityNote = PriorityNote.HIGH_PRIORITY,
  val timeNote: TimeNoteUiModel,
) {
  @Serializable
  data class TimeNoteUiModel(
    val monthNumber: Int,
    val yearNumber: Int,
    val dayNumber: Int,
    val dayName: String,
    val timeCreateMillSecond: Long,
  )
}
