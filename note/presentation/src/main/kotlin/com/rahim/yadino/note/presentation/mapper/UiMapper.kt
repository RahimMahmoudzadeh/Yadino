package com.rahim.yadino.note.presentation.mapper

import com.rahim.yadino.note.domain.model.Note
import com.rahim.yadino.note.presentation.model.NoteUiModel
import com.rahim.yadino.note.presentation.model.PriorityNote

fun Note.toNoteUiModel(): NoteUiModel =
  NoteUiModel(
    id = this.id,
    name = this.name,
    description = this.description,
    isChecked = this.isChecked,
    state = checkState(this.state),
    timeCreate = this.timeInMileSecond.toString(),
  )

fun checkState(state: Int): PriorityNote {
  return when (state) {
    PriorityNote.HIGH_PRIORITY.state -> PriorityNote.HIGH_PRIORITY
    PriorityNote.NORMAL.state -> PriorityNote.NORMAL
    else -> PriorityNote.LOW_PRIORITY
  }
}
