package com.rahim.yadino.note.data.mapper

import com.rahim.yadino.db.note.model.NoteEntity
import com.rahim.yadino.note.domain.model.Note


fun Note.toNoteEntity(): NoteEntity = NoteEntity(
  id = id,
  name = name,
  description = description,
  isChecked = isChecked,
  state = state,
  dayName = dayName,
  dayNumber = dayNumber,
  monthNumber = monthNumber,
  yearNumber = yearNumber,
  isSample = isSample,
  timeInMileSecond = timeInMileSecond,
)

fun NoteEntity.toNoteModel(): Note = Note(id = id, name = name, description = description, isChecked = isChecked, state = state, dayName = dayName, dayNumber = dayNumber, monthNumber = monthNumber, yearNumber = yearNumber, isSample = isSample, timeInMileSecond = timeInMileSecond)
