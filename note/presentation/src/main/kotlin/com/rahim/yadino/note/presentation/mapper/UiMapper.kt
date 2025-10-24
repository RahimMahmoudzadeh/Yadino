package com.rahim.yadino.note.presentation.mapper

import com.rahim.yadino.note.domain.model.NameNote
import com.rahim.yadino.note.domain.model.Note
import com.rahim.yadino.note.presentation.model.NameNoteUi
import com.rahim.yadino.note.presentation.model.NoteUiModel
import com.rahim.yadino.note.presentation.model.PriorityNote

fun Note.toNoteUiModel(): NoteUiModel =
  NoteUiModel(
    id = this.id,
    name = this.name,
    description = this.description,
    isChecked = this.isChecked,
    state = checkState(this.state),
    timeNote = NoteUiModel.TimeNoteUiModel(timeCreateMillSecond = this.timeInMileSecond ?: 0, yearNumber = this.dayNumber ?: 0, monthNumber = this.monthNumber ?: 0, dayNumber = this.yearNumber ?: 0, dayName = this.dayName),
  )

fun NoteUiModel.toNote(): Note =
  Note(
    id = this.id,
    name = this.name,
    description = this.description,
    isChecked = this.isChecked,
    state = this.state.state,
    dayName = this.timeNote.dayName,
    timeInMileSecond = this.timeNote.timeCreateMillSecond,
    dayNumber = this.timeNote.dayNumber,
    monthNumber = this.timeNote.monthNumber,
    yearNumber = this.timeNote.yearNumber,
  )

fun checkState(state: Int): PriorityNote {
  return when (state) {
    PriorityNote.HIGH_PRIORITY.state -> PriorityNote.HIGH_PRIORITY
    PriorityNote.NORMAL.state -> PriorityNote.NORMAL
    else -> PriorityNote.LOW_PRIORITY
  }
}

fun NameNoteUi.toNameNote(): NameNote = NameNote(name = name)
