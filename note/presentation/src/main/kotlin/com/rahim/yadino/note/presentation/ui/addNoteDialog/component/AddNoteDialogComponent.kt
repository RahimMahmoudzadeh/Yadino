package com.rahim.yadino.note.presentation.ui.addNoteDialog.component

import androidx.compose.runtime.Immutable
import com.rahim.yadino.base.UnidirectionalComponent
import com.rahim.yadino.enums.message.MessageUi
import com.rahim.yadino.note.presentation.model.NoteUiModel

interface AddNoteDialogComponent : UnidirectionalComponent<AddNoteDialogComponent.Event, AddNoteDialogComponent.State, AddNoteDialogComponent.Effect> {
  @Immutable
  sealed class Event {
    data class CreateNote(val note: NoteUiModel) : Event()
    data object Dismiss : Event()
  }

  @Immutable
  sealed class Effect {
    data class ShowToast(val messageUi: MessageUi) : Effect()
  }

  @Immutable
  data class State(val date: String = "")
}
