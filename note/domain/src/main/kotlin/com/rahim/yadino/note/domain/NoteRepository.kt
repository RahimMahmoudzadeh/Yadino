package com.rahim.yadino.note.domain

import com.rahim.yadino.note.domain.model.NoteModel
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
  suspend fun addNote(noteModel: NoteModel)
  suspend fun updateNote(noteModel: NoteModel)
  suspend fun deleteNote(noteModel: NoteModel)
  fun getNotes(): Flow<List<NoteModel>>
  suspend fun addSampleNote()
  fun searchNote(name: String): Flow<List<NoteModel>>
}
