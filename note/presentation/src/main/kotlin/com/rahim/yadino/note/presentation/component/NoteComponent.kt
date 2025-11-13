package com.rahim.yadino.note.presentation.component

import androidx.compose.runtime.Immutable
import com.rahim.yadino.base.LoadableData
import com.rahim.yadino.base.UnidirectionalComponent
import com.rahim.yadino.note.presentation.component.addNoteDialog.AddNoteDialogComponent
import com.rahim.yadino.note.presentation.model.NameNoteUi
import com.rahim.yadino.note.presentation.model.NoteUiModel
import kotlinx.collections.immutable.PersistentList

interface NoteComponent : UnidirectionalComponent<NoteComponent.Event, NoteComponent.State> {

  @Immutable
  sealed class Event() {
    data class Search(val nameNoteUi: NameNoteUi) : Event()
    data class Delete(val deleteNote: NoteUiModel) : Event()
    data class OnChecked(val checkedNote: NoteUiModel) : Event()
    data class OnOpenUpdateNoteDialog(val updateNote: NoteUiModel) : Event()
    data object GetNotes : Event()
  }

  @Immutable
  data class State(
    val notes: LoadableData<PersistentList<NoteUiModel>> = LoadableData.Initial,
  )
}
