package com.rahim.data.repository.note

import com.rahim.yadino.routine.modle.note.NoteModel
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    suspend fun addNote(noteModel: com.rahim.yadino.routine.modle.note.NoteModel)
    suspend fun updateNote(noteModel: com.rahim.yadino.routine.modle.note.NoteModel)
    suspend fun deleteNote(noteModel: com.rahim.yadino.routine.modle.note.NoteModel)
    fun getNotes(): Flow<List<com.rahim.yadino.routine.modle.note.NoteModel>>
    suspend fun addSampleNote()
    fun searchNote(
        name: String
    ): Flow<List<com.rahim.yadino.routine.modle.note.NoteModel>>
}