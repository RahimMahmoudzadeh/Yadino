package com.rahim.yadino.note_repository.mapper

import com.rahim.yadino.base.db.model.LocalNoteDto
import com.rahim.yadino.note.model.NoteModel

fun LocalNoteDto.toNote(): NoteModel = NoteModel(
    id = id,
    name = name,
    dayName = dayName,
    dayNumber = dayNumber,
    monthNumber = monthNumber,
    yerNumber = yerNumber,
    isSample = isSample,
    isChecked = isChecked,
    timeInMileSecond = timeInMileSecond,
    description = description,
    state = state,

    )

fun NoteModel.toLocalNoteDto(): LocalNoteDto = LocalNoteDto(
    id = id,
    name = name,
    dayName = dayName,
    dayNumber = dayNumber,
    monthNumber = monthNumber,
    yerNumber = yerNumber,
    isSample = isSample,
    isChecked = isChecked,
    timeInMileSecond = timeInMileSecond,
    description = description,
    state = state,
)