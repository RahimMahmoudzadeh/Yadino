package com.rahim.yadino.note.presentation.ui.root.component


import com.rahim.yadino.note.presentation.model.ErrorDialogUiModel
import com.rahim.yadino.note.presentation.model.NoteUiModel
import kotlinx.serialization.Serializable

sealed interface DialogSlotNoteComponent {
  @Serializable
  data object AddNoteDialog : DialogSlotNoteComponent

  @Serializable
  data class UpdateNoteDialog(val updateNote: NoteUiModel) : DialogSlotNoteComponent

  @Serializable
  data class ErrorDialogNote(val errorDialogUiModel: ErrorDialogUiModel) : DialogSlotNoteComponent

}
