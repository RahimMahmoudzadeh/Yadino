package com.rahim.yadino.note.presentation

import androidx.compose.runtime.Immutable
import com.rahim.yadino.base.UnidirectionalViewModel
import com.rahim.yadino.enums.error.ErrorMessageCode
import com.rahim.yadino.note.domain.model.Note

interface NoteContract : UnidirectionalViewModel<NoteContract.NoteEvent, NoteContract.NoteState> {

  @Immutable
  sealed class NoteEvent() {
    data class SearchNote(val searchText: String) : NoteEvent()
    data class DeleteNote(val deleteNote: Note) : NoteEvent()
    data class UpdateNote(val updateNote: Note) : NoteEvent()
    data class AddNote(val addNote: Note) : NoteEvent()
    data object GetNotes : NoteEvent()
  }

  @Immutable
  data class NoteState(
      val isLoading: Boolean = false,
      val notes: List<Note> = emptyList(),
      val nameDay: String? = null,
      val errorMessage: ErrorMessageCode? = null,
  )
}
