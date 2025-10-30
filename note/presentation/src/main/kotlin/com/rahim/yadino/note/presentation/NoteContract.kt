package com.rahim.yadino.note.presentation

import androidx.compose.runtime.Immutable
import com.rahim.yadino.base.LoadableData
import com.rahim.yadino.base.UnidirectionalComponent
import com.rahim.yadino.note.presentation.model.NameNoteUi
import com.rahim.yadino.note.presentation.model.NoteUiModel
import kotlinx.collections.immutable.PersistentList

interface NoteContract : UnidirectionalComponent<NoteContract.NoteEvent, NoteContract.NoteState> {

  @Immutable
  sealed class NoteEvent() {
    data class SearchNote(val nameNoteUi: NameNoteUi) : NoteEvent()
    data class DeleteNote(val deleteNote: NoteUiModel) : NoteEvent()
    data class UpdateNote(val updateNote: NoteUiModel) : NoteEvent()
    data class AddNote(val addNote: NoteUiModel) : NoteEvent()
    data object GetNotes : NoteEvent()
  }

  @Immutable
  data class NoteState(
    val notes: LoadableData<PersistentList<NoteUiModel>> = LoadableData.Initial,
  )
}
