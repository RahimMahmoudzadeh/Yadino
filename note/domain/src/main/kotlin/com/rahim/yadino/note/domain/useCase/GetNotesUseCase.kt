package com.rahim.yadino.note.domain.useCase

import com.rahim.yadino.note.domain.NoteRepository
import com.rahim.yadino.note.domain.model.Note
import kotlinx.coroutines.flow.Flow

class GetNotesUseCase(private val noteRepository: NoteRepository) {
  suspend operator fun invoke(note: Note): Flow<List<Note>> {
    return noteRepository.getNotes()
  }
}
