package com.rahim.yadino.note.presentation.mapper

import com.rahim.yadino.note.domain.model.Note
import com.rahim.yadino.note.presentation.model.NoteUiModel
import com.rahim.yadino.note.presentation.model.StateNote

fun Note.toNoteUiModel(): NoteUiModel =
  NoteUiModel(
    id = this.id,
    name = this.name,
    description = this.description,
    isChecked = this.isChecked,
    state = checkState(this.state),
    timeCreate = this.timeInMileSecond.toString(),
  )

fun checkState(state: Int): StateNote {
  return when (state) {
    StateNote.HIGH_PRIORITY.state -> StateNote.HIGH_PRIORITY
    StateNote.NORMAL.state -> StateNote.NORMAL
    else -> StateNote.LOW_PRIORITY
  }
}
