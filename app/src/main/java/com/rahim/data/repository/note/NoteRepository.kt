package com.rahim.data.repository.note

import com.rahim.data.model.note.NoteModel
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
  suspend fun addNote(noteModel: NoteModel)
  suspend fun updateNote(noteModel: NoteModel)
  suspend fun deleteNote(noteModel: NoteModel)
  fun getNotes(): Flow<List<NoteModel>>
  suspend fun addSampleNote()
  fun searchNote(
    name: String,
  ): Flow<List<NoteModel>>
}
