package com.rahim.yadino.note.presentation.ui.root.component

import androidx.compose.runtime.Immutable
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import com.rahim.yadino.base.LoadableData
import com.rahim.yadino.base.UnidirectionalComponent
import com.rahim.yadino.enums.message.MessageUi
import com.rahim.yadino.note.presentation.model.ErrorDialogRemoveNoteUiModel
import com.rahim.yadino.note.presentation.model.NameNoteUi
import com.rahim.yadino.note.presentation.model.NoteUiModel
import com.rahim.yadino.note.presentation.ui.addNoteDialog.component.AddNoteDialogComponent
import com.rahim.yadino.note.presentation.ui.errorDialog.component.ErrorDialogComponent
import com.rahim.yadino.note.presentation.ui.main.component.NoteMainComponent
import com.rahim.yadino.note.presentation.ui.updateNoteDialog.component.UpdateNoteDialogComponent
import kotlinx.collections.immutable.PersistentList
import kotlinx.serialization.Serializable

interface RootNoteComponent {

  val stack: Value<com.arkivanov.decompose.router.stack.ChildStack<*, ChildStack>>

  val addNoteDialogScreen: Value<ChildSlot<DialogSlotNoteComponent.AddNoteDialog, AddNoteDialogComponent>>
  val updateNoteDialogScreen: Value<ChildSlot<DialogSlotNoteComponent.UpdateNoteDialog, UpdateNoteDialogComponent>>
  val errorDialogRemoveNoteScreen: Value<ChildSlot<DialogSlotNoteComponent.ErrorDialogNote, ErrorDialogComponent>>

  sealed interface ChildStack {
    class NoteMainStack(val component: NoteMainComponent) : ChildStack
  }

  sealed interface DialogSlotNoteComponent {
    @Serializable
    data object AddNoteDialog : DialogSlotNoteComponent

    @Serializable
    data class UpdateNoteDialog(val updateNote: NoteUiModel) : DialogSlotNoteComponent

    @Serializable
    data class ErrorDialogNote(val errorDialogRemoveNoteUiModel: ErrorDialogRemoveNoteUiModel) : DialogSlotNoteComponent
  }

  @Serializable
  sealed interface ChildConfig {
    @Serializable
    data object NoteMain : ChildConfig
  }
}
