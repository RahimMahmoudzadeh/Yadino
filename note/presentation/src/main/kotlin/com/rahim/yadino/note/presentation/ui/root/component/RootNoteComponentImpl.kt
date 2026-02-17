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
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.rahim.yadino.base.LoadableData
import com.rahim.yadino.note.domain.useCase.AddNoteUseCase
import com.rahim.yadino.note.domain.useCase.DeleteNoteUseCase
import com.rahim.yadino.note.domain.useCase.GetNotesUseCase
import com.rahim.yadino.note.domain.useCase.SearchNoteUseCase
import com.rahim.yadino.note.domain.useCase.UpdateNoteUseCase
import com.rahim.yadino.note.presentation.mapper.toNameNote
import com.rahim.yadino.note.presentation.mapper.toNote
import com.rahim.yadino.note.presentation.mapper.toNoteUiModel
import com.rahim.yadino.note.presentation.model.ErrorDialogRemoveNoteUiModel
import com.rahim.yadino.note.presentation.model.NameNoteUi
import com.rahim.yadino.note.presentation.model.NoteUiModel
import com.rahim.yadino.note.presentation.ui.addNoteDialog.component.AddNoteDialogComponent
import com.rahim.yadino.note.presentation.ui.addNoteDialog.component.AddNoteDialogComponentImpl
import com.rahim.yadino.note.presentation.ui.errorDialog.component.ErrorDialogComponent
import com.rahim.yadino.note.presentation.ui.errorDialog.component.ErrorDialogComponentImpl
import com.rahim.yadino.note.presentation.ui.main.component.NoteMainComponent
import com.rahim.yadino.note.presentation.ui.main.component.NoteMainComponentImpl
import com.rahim.yadino.note.presentation.ui.updateNoteDialog.component.UpdateNoteDialogComponent
import com.rahim.yadino.note.presentation.ui.updateNoteDialog.component.UpdateNoteDialogComponentImpl
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class RootNoteComponentImpl(
  componentContext: ComponentContext,
  mainContext: CoroutineContext,
  private val getNotesUseCase: GetNotesUseCase,
  private val searchNoteUseCase: SearchNoteUseCase,
  private val addNoteUseCase: AddNoteUseCase,
  private val deleteNoteUseCase: DeleteNoteUseCase,
  private val updateNoteUseCase: UpdateNoteUseCase,
) : RootNoteComponent, ComponentContext by componentContext {

  private val addNoteDialogComponentNavigationSlot =
    SlotNavigation<RootNoteComponent.DialogSlotNoteComponent.AddNoteDialog>()

  private val updateNoteDialogComponentNavigationSlot =
    SlotNavigation<RootNoteComponent.DialogSlotNoteComponent.UpdateNoteDialog>()

  private val errorDialogNoteComponentNavigationSlot =
    SlotNavigation<RootNoteComponent.DialogSlotNoteComponent.ErrorDialogNote>()

  override val addNoteDialogScreen: Value<ChildSlot<RootNoteComponent.DialogSlotNoteComponent.AddNoteDialog, AddNoteDialogComponent>> =
    childSlot(
      source = addNoteDialogComponentNavigationSlot,
      serializer = RootNoteComponent.DialogSlotNoteComponent.AddNoteDialog.serializer(),
      handleBackButton = true,
      key = "addNoteDialogComponentNavigationSlot",
    ) { _, childComponentContext ->
      AddNoteDialogComponentImpl(
        componentContext = childComponentContext,
        mainDispatcher = Dispatchers.Main,
        ioDispatcher = Dispatchers.IO,
        addNoteUseCase = addNoteUseCase,
        onDismissed = addNoteDialogComponentNavigationSlot::dismiss,
      )
    }

  override val updateNoteDialogScreen: Value<ChildSlot<RootNoteComponent.DialogSlotNoteComponent.UpdateNoteDialog, UpdateNoteDialogComponent>> =
    childSlot(
      source = updateNoteDialogComponentNavigationSlot,
      serializer = RootNoteComponent.DialogSlotNoteComponent.UpdateNoteDialog.serializer(),
      handleBackButton = true,
      key = "updateNoteDialogComponentNavigationSlot",
    ) { config, childComponentContext ->
      UpdateNoteDialogComponentImpl(
        componentContext = childComponentContext,
        mainDispatcher = Dispatchers.Main,
        ioDispatcher = Dispatchers.IO,
        updateNoteUseCase = updateNoteUseCase,
        updateNote = config.updateNote,
        onDismissed = updateNoteDialogComponentNavigationSlot::dismiss,
      )
    }
  override val errorDialogRemoveNoteScreen: Value<ChildSlot<RootNoteComponent.DialogSlotNoteComponent.ErrorDialogNote, ErrorDialogComponent>> =
    childSlot(
      source = errorDialogNoteComponentNavigationSlot,
      serializer = RootNoteComponent.DialogSlotNoteComponent.ErrorDialogNote.serializer(),
      handleBackButton = true,
      key = "errorDialogNoteComponentNavigationSlot",
    ) { config, childComponentContext ->
      ErrorDialogComponentImpl(
        componentContext = childComponentContext,
        mainContext = Dispatchers.Main,
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
    showAddNoteDialog = ::onShowAddNoteDialog,
    showErrorDialog = ::showErrorDialog,
    showUpdateNoteDialog = ::showUpdateNoteDialog,
  )

  private fun showErrorDialog(errorDialogRemoveNoteUiModel: ErrorDialogRemoveNoteUiModel) {
    errorDialogNoteComponentNavigationSlot.activate(RootNoteComponent.DialogSlotNoteComponent.ErrorDialogNote(errorDialogRemoveNoteUiModel))
  }

  private fun showUpdateNoteDialog(updateNote: NoteUiModel) {
    updateNoteDialogComponentNavigationSlot.activate(RootNoteComponent.DialogSlotNoteComponent.UpdateNoteDialog(updateNote))
  }

  fun onShowAddNoteDialog() {
    addNoteDialogComponentNavigationSlot.activate(RootNoteComponent.DialogSlotNoteComponent.AddNoteDialog)
  }
}
