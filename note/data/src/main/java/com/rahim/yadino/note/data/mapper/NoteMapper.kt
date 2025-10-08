package com.rahim.yadino.note.data.mapper

import com.rahim.yadino.db.note.model.NoteEntity
import com.rahim.yadino.note.domain.model.NoteModel


fun NoteModel.toNoteEntity(): NoteEntity = NoteEntity(
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

fun NoteEntity.toNoteModel(): NoteModel = NoteModel(id = id, name = name, description = description, isChecked = isChecked, state = state, dayName = dayName, dayNumber = dayNumber, monthNumber = monthNumber, yearNumber = yearNumber, isSample = isSample, timeInMileSecond = timeInMileSecond)
