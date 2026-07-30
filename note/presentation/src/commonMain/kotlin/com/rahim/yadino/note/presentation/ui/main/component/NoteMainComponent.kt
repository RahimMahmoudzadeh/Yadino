package com.rahim.yadino.note.presentation.ui.main.component

import androidx.compose.runtime.Immutable
import com.rahim.yadino.note.presentation.model.ErrorDialogRemoveNoteUiModel
import com.rahim.yadino.note.presentation.model.NameNoteUi
import com.rahim.yadino.note.presentation.model.NoteUiModel

//interface NoteMainComponent : UnidirectionalComponent<NoteMainComponent.Event, NoteMainComponent.State, NoteMainComponent.Effect> {
//  @Immutable
//  sealed class Event() {
//    data class Search(val nameNoteUi: NameNoteUi) : Event()
//    data class ShowErrorRemoveNoteDialog(val errorDialogRemoveNoteUiModel: ErrorDialogRemoveNoteUiModel) : Event()
//    data class OnChecked(val checkedNote: NoteUiModel) : Event()
//    data class OnOpenUpdateNoteDialog(val updateNote: NoteUiModel) : Event()
//    data object GetNotes : Event()
//  }
//
//  @Immutable
//  sealed class Effect {
//    data class ShowToast(val message: MessageUi) : Effect()
//  }
//
//  @Immutable
//  data class State(
//    val notes: LoadableData<PersistentList<NoteUiModel>> = LoadableData.Initial,
//  )
//}
