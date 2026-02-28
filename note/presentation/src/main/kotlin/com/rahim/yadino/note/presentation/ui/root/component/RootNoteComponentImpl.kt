package com.rahim.yadino.note.presentation.ui.root.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.rahim.yadino.note.domain.useCase.AddNoteUseCase
import com.rahim.yadino.note.domain.useCase.DeleteNoteUseCase
import com.rahim.yadino.note.domain.useCase.GetNotesUseCase
import com.rahim.yadino.note.domain.useCase.SearchNoteUseCase
import com.rahim.yadino.note.domain.useCase.UpdateNoteUseCase
import com.rahim.yadino.note.presentation.model.ErrorDialogRemoveNoteUiModel
import com.rahim.yadino.note.presentation.model.NoteUiModel
import com.rahim.yadino.note.presentation.ui.errorDialog.component.ErrorDialogComponent
import com.rahim.yadino.note.presentation.ui.errorDialog.component.ErrorDialogComponentImpl
import com.rahim.yadino.note.presentation.ui.main.component.NoteMainComponent
import com.rahim.yadino.note.presentation.ui.main.component.NoteMainComponentImpl
import com.rahim.yadino.note.presentation.ui.updateNoteDialog.component.UpdateNoteDialogComponent
import com.rahim.yadino.note.presentation.ui.updateNoteDialog.component.UpdateNoteDialogComponentImpl
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class RootNoteComponentImpl(
  componentContext: ComponentContext,
  mainContext: CoroutineContext,
  ioContext: CoroutineContext,
  private val getNotesUseCase: GetNotesUseCase,
  private val searchNoteUseCase: SearchNoteUseCase,
  private val deleteNoteUseCase: DeleteNoteUseCase,
  private val updateNoteUseCase: UpdateNoteUseCase,
) : RootNoteComponent, ComponentContext by componentContext {

  private val updateNoteDialogComponentNavigationSlot =
    SlotNavigation<RootNoteComponent.DialogSlot.UpdateNoteDialog>()

  private val errorDialogNoteComponentNavigationSlot =
    SlotNavigation<RootNoteComponent.DialogSlot.ErrorDialogNote>()

  override val updateNoteDialogScreen: Value<ChildSlot<RootNoteComponent.DialogSlot.UpdateNoteDialog, UpdateNoteDialogComponent>> =
    childSlot(
      source = updateNoteDialogComponentNavigationSlot,
      serializer = RootNoteComponent.DialogSlot.UpdateNoteDialog.serializer(),
      handleBackButton = true,
      key = "updateNoteDialogComponentNavigationSlot",
    ) { config, childComponentContext ->
      UpdateNoteDialogComponentImpl(
        componentContext = childComponentContext,
        mainContext = mainContext,
        ioContext = ioContext,
        updateNoteUseCase = updateNoteUseCase,
        updateNote = config.updateNote,
        onDismissed = updateNoteDialogComponentNavigationSlot::dismiss,
      )
    }
  override val errorDialogRemoveNoteScreen: Value<ChildSlot<RootNoteComponent.DialogSlot.ErrorDialogNote, ErrorDialogComponent>> =
    childSlot(
      source = errorDialogNoteComponentNavigationSlot,
      serializer = RootNoteComponent.DialogSlot.ErrorDialogNote.serializer(),
      handleBackButton = true,
      key = "errorDialogNoteComponentNavigationSlot",
    ) { config, childComponentContext ->
      ErrorDialogComponentImpl(
        componentContext = childComponentContext,
        mainContext = mainContext,
        deleteNoteUseCase = deleteNoteUseCase,
        errorDialogRemoveNoteUiModel = config.errorDialogRemoveNoteUiModel,
        onDismissed = errorDialogNoteComponentNavigationSlot::dismiss,
      )
    }

  private val navigation = StackNavigation<RootNoteComponent.ChildConfig>()

  override val stack: Value<ChildStack<*, RootNoteComponent.ChildStack>> = childStack(
    source = navigation,
    serializer = RootNoteComponent.ChildConfig.serializer(),
    initialConfiguration = RootNoteComponent.ChildConfig.NoteMain,
    handleBackButton = true,
    childFactory = ::childComponent,
  )

  private fun childComponent(
    config: RootNoteComponent.ChildConfig,
    childComponentContext: ComponentContext,
  ): RootNoteComponent.ChildStack = when (config) {
    RootNoteComponent.ChildConfig.NoteMain -> RootNoteComponent.ChildStack.NoteMainStack(component = noteComponent(componentContext = childComponentContext))
  }

  private fun noteComponent(componentContext: ComponentContext): NoteMainComponent = NoteMainComponentImpl(
    componentContext = componentContext,
    mainContext = Dispatchers.Main,
    getNotesUseCase = getNotesUseCase,
    searchNoteUseCase = searchNoteUseCase,
    updateNoteUseCase = updateNoteUseCase,
    showErrorDialog = ::showErrorDialog,
    showUpdateNoteDialog = ::showUpdateNoteDialog,
  )

  private fun showErrorDialog(errorDialogRemoveNoteUiModel: ErrorDialogRemoveNoteUiModel) {
    errorDialogNoteComponentNavigationSlot.activate(RootNoteComponent.DialogSlot.ErrorDialogNote(errorDialogRemoveNoteUiModel))
  }

  private fun showUpdateNoteDialog(updateNote: NoteUiModel) {
    updateNoteDialogComponentNavigationSlot.activate(RootNoteComponent.DialogSlot.UpdateNoteDialog(updateNote))
  }
}
