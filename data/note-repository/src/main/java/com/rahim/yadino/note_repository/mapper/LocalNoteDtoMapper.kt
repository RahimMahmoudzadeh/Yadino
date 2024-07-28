package com.rahim.yadino.note_repository.mapper

import com.rahim.yadino.note.model.NoteModel
import com.rahim.yadino.note_local.dto.LocalNoteDto

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