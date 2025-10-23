package com.rahim.yadino.note.domain.useCase

import com.rahim.yadino.note.domain.NoteRepository
import com.rahim.yadino.note.domain.model.Note

class UpdateNoteUseCase(private val noteRepository: NoteRepository) {
  suspend operator fun invoke(note: Note) {
    noteRepository.updateNote(note)
  }
}
