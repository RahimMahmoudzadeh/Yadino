package com.rahim.yadino.note.presentation.component

import androidx.compose.runtime.Immutable
import com.rahim.yadino.base.LoadableData
import com.rahim.yadino.base.UnidirectionalComponent
import com.rahim.yadino.note.presentation.model.NameNoteUi
import com.rahim.yadino.note.presentation.model.NoteUiModel
import kotlinx.collections.immutable.PersistentList

interface NoteComponent : UnidirectionalComponent<NoteComponent.NoteEvent, NoteComponent.NoteState> {

  @Immutable
  sealed class NoteEvent() {
    data class SearchNote(val nameNoteUi: NameNoteUi) : NoteEvent()
    data class DeleteNote(val deleteNote: NoteUiModel) : NoteEvent()
    data class OnCheckedNote(val checkedNote: NoteUiModel) : NoteEvent()
    data object GetNotes : NoteEvent()
  }

  @Immutable
  data class NoteState(
    val notes: LoadableData<PersistentList<NoteUiModel>> = LoadableData.Initial,
  )
}
