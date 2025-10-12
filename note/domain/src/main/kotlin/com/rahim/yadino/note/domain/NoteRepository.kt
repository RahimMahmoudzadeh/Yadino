package com.rahim.yadino.note.domain

import com.rahim.yadino.note.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
  suspend fun addNote(note: Note)
  suspend fun updateNote(note: Note)
  suspend fun deleteNote(note: Note)
  fun getNotes(): Flow<List<Note>>
  suspend fun addSampleNote()
  fun searchNote(name: String): Flow<List<Note>>
}
