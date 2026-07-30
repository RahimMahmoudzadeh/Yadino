package com.rahim.yadino.note.presentation.ui.root.component

import com.rahim.yadino.note.presentation.model.ErrorDialogRemoveNoteUiModel
import com.rahim.yadino.note.presentation.model.NoteUiModel
import kotlinx.serialization.Serializable

//interface RootNoteComponent {
//
//  val stack: Value<com.arkivanov.decompose.router.stack.ChildStack<*, ChildStack>>
//
//  val updateNoteDialogScreen: Value<ChildSlot<DialogSlot.UpdateNoteDialog, UpdateNoteDialogComponent>>
//  val errorDialogRemoveNoteScreen: Value<ChildSlot<DialogSlot.ErrorDialogNote, ErrorDialogComponent>>
//
//  sealed interface ChildStack {
//    class NoteMainStack(val component: NoteMainComponent) : ChildStack
//  }
//
//  sealed interface DialogSlot {
//    @Serializable
//    data class UpdateNoteDialog(val updateNote: NoteUiModel) : DialogSlot
//
//    @Serializable
//    data class ErrorDialogNote(val errorDialogRemoveNoteUiModel: ErrorDialogRemoveNoteUiModel) : DialogSlot
//  }
//
//  @Serializable
//  sealed interface ChildConfig {
//    @Serializable
//    data object NoteMain : ChildConfig
//  }
//}
