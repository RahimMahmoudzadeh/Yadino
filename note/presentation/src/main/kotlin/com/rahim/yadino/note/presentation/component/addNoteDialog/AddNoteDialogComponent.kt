package com.rahim.yadino.note.presentation.component.addNoteDialog

import androidx.compose.runtime.Immutable
import com.rahim.yadino.base.UnidirectionalComponent
import com.rahim.yadino.note.presentation.model.NoteUiModel

interface AddNoteDialogComponent : UnidirectionalComponent<AddNoteDialogComponent.Event, AddNoteDialogComponent.State, Nothing> {
  @Immutable
  sealed class Event {
    data class CreateNote(val note: NoteUiModel) : Event()
    data class UpdateNote(val note: NoteUiModel) : Event()
    data object Dismiss : Event()
  }

  @Immutable
  data class State(val updateNote: NoteUiModel? = null)
}
