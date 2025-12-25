package com.rahim.yadino.note.presentation.component.updateNoteDialog

import androidx.compose.runtime.Immutable
import com.rahim.yadino.base.UnidirectionalComponent
import com.rahim.yadino.enums.message.MessageUi
import com.rahim.yadino.note.presentation.model.NoteUiModel

interface UpdateNoteDialogComponent : UnidirectionalComponent<UpdateNoteDialogComponent.Event, UpdateNoteDialogComponent.State, UpdateNoteDialogComponent.Effect> {
  @Immutable
  sealed class Event {
    data class UpdateNote(val note: NoteUiModel) : Event()
    data object Dismiss : Event()
  }

  @Immutable
  sealed class Effect {
    data class ShowToast(val messageUi: MessageUi) : Effect()
  }

  @Immutable
  data class State(val updateNote: NoteUiModel? = null)
}
