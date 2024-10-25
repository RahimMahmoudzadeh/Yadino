package com.rahim.yadino.note

import androidx.compose.runtime.Immutable
import com.rahim.yadino.base.UnidirectionalViewModel
import com.rahim.yadino.enums.error.ErrorMessageCode
import com.rahim.yadino.model.NoteModel

interface NoteContract : UnidirectionalViewModel<NoteContract.NoteEvent, NoteContract.NoteState> {

  @Immutable
  sealed class NoteEvent() {
    data class SearchNote(val searchText: String) : NoteEvent()
    data class DeleteNote(val deleteNote: NoteModel) : NoteEvent()
    data class UpdateNote(val updateNote: NoteModel) : NoteEvent()
    data class AddNote(val addNote: NoteModel) : NoteEvent()
    data object GetNotes : NoteEvent()
  }

  @Immutable
  data class NoteState(
    val isLoading: Boolean = false,
    val notes: List<NoteModel> = emptyList(),
    val nameDay: String? = null,
    val errorMessage: ErrorMessageCode? = null,
  )
}
