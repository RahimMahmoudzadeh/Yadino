package com.rahim.yadino.noteRepository.mapper

import com.rahim.yadino.note.model.NoteModel
import com.rahim.yadino.noteRepository.model.NoteEntity

fun NoteModel.toNoteEntity(): NoteEntity = NoteEntity(id = id, name = name, description = description, isChecked = isChecked, state = state, dayName = dayName, dayNumber = dayNumber, monthNumber = monthNumber, yearNumber = yearNumber, isSample = isSample, timeInMileSecond = timeInMileSecond)

fun NoteEntity.toNoteModel(): NoteModel = NoteModel(id = id, name = name, description = description, isChecked = isChecked, state = state, dayName = dayName, dayNumber = dayNumber, monthNumber = monthNumber, yearNumber = yearNumber, isSample = isSample, timeInMileSecond = timeInMileSecond)
