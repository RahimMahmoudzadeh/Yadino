package com.rahim.yadino.note.presentation.model

data class NoteUiModel(
  val id: Int? = null,
  val name: String,
  val description: String,
  val isChecked: Boolean = false,
  val state: StateNote = StateNote.HIGH_PRIORITY,
  val timeCreate: String,
)
