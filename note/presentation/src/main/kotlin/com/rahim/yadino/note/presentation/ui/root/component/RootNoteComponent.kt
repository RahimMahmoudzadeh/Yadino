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
import com.rahim.yadino.note.presentation.ui.updateNoteDialog.component.UpdateNoteDialogComponent
import kotlinx.collections.immutable.PersistentList

interface RootNoteComponent : UnidirectionalComponent<RootNoteComponent.Event, RootNoteComponent.State, RootNoteComponent.Effect> {

  @Immutable
  sealed class Event() {
    data class Search(val nameNoteUi: NameNoteUi) : Event()
    data class ShowErrorRemoveNoteDialog(val errorDialogRemoveNoteUiModel: ErrorDialogRemoveNoteUiModel) : Event()
    data class OnChecked(val checkedNote: NoteUiModel) : Event()
    data class OnOpenUpdateNoteDialog(val updateNote: NoteUiModel) : Event()
    data object GetNotes : Event()
    data object OnShowAddNoteDialog : Event()
  }

  @Immutable
  sealed class Effect {
    data class ShowToast(val message: MessageUi) : Effect()
  }

  @Immutable
  data class State(
    val notes: LoadableData<PersistentList<NoteUiModel>> = LoadableData.Initial,
  )

  val addNoteDialogScreen: Value<ChildSlot<DialogSlotNoteComponent.AddNoteDialog, AddNoteDialogComponent>>
  val updateNoteDialogScreen: Value<ChildSlot<DialogSlotNoteComponent.UpdateNoteDialog, UpdateNoteDialogComponent>>
  val errorDialogRemoveNoteScreen: Value<ChildSlot<DialogSlotNoteComponent.ErrorDialogNote, ErrorDialogComponent>>

  fun onShowAddNoteDialog()
}
