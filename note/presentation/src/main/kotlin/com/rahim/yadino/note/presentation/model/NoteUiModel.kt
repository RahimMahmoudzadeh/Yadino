package com.rahim.yadino.note.presentation.model

data class NoteUiModel(
  val id: Int? = null,
  val name: String,
  val description: String,
  val isChecked: Boolean = false,
  val state: PriorityNote = PriorityNote.HIGH_PRIORITY,
  val timeCreate: String,
  val timeNote: TimeNoteUiModel,
) {
  data class TimeNoteUiModel(
    val monthNumber: Int,
    val yearNumber: Int,
    val dayNumber: Int,
    val dayName: String,
  )
}
