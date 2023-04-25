package com.rahim.data.repository.note

import com.rahim.data.modle.note.NoteModel
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    suspend fun addNote(noteModel: NoteModel)

    fun getNotes(): Flow<List<NoteModel>>
}