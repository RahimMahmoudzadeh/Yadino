package com.rahim.yadino.note.domain.useCase

import com.rahim.yadino.note.domain.NoteRepository
import com.rahim.yadino.note.domain.model.NameNote
import com.rahim.yadino.note.domain.model.Note
import kotlinx.coroutines.flow.Flow

class SearchNoteUseCase(private val noteRepository: NoteRepository) {
  operator fun invoke(nameNote: NameNote): Flow<List<Note>> {
    return noteRepository.searchNote(nameNote)
  }
}
