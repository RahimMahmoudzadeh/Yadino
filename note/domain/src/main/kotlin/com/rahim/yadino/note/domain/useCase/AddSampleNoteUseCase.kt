package com.rahim.yadino.note.domain.useCase

import com.rahim.yadino.note.domain.NoteRepository

class AddSampleNoteUseCase(private val noteRepository: NoteRepository) {
  suspend operator fun invoke() {
    noteRepository.addSampleNote()
  }
}
