package com.rahim.yadino.note.presentation.ui.root.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
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
import com.rahim.yadino.note.presentation.model.ErrorDialogUiModel
import com.rahim.yadino.note.presentation.model.NameNoteUi
import com.rahim.yadino.note.presentation.model.NoteUiModel
import com.rahim.yadino.note.presentation.ui.addNoteDialog.component.AddNoteDialogComponent
import com.rahim.yadino.note.presentation.ui.addNoteDialog.component.AddNoteDialogComponentImpl
import com.rahim.yadino.note.presentation.ui.errorDialog.component.ErrorDialogComponent
import com.rahim.yadino.note.presentation.ui.errorDialog.component.ErrorDialogComponentImpl
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

class NoteRootComponentImpl(
  componentContext: ComponentContext,
  mainContext: CoroutineContext,
  private val getNotesUseCase: GetNotesUseCase,
  private val searchNoteUseCase: SearchNoteUseCase,
  private val addNoteUseCase: AddNoteUseCase,
  private val deleteNoteUseCase: DeleteNoteUseCase,
  private val updateNoteUseCase: UpdateNoteUseCase,
) : NoteRootComponent, ComponentContext by componentContext {

  private val scope: CoroutineScope = coroutineScope(mainContext + SupervisorJob())

  private var _state = MutableValue(NoteRootComponent.State())
  override val state: Value<NoteRootComponent.State> = _state

  private val _effect: Channel<NoteRootComponent.Effect> = Channel(Channel.BUFFERED)
  override val effect: Flow<NoteRootComponent.Effect> = _effect.consumeAsFlow()

  private val addNoteDialogComponentNavigationSlot =
    SlotNavigation<DialogSlotNoteComponent.AddNoteDialog>()

  private val updateNoteDialogComponentNavigationSlot =
    SlotNavigation<DialogSlotNoteComponent.UpdateNoteDialog>()

  private val errorDialogNoteComponentNavigationSlot =
    SlotNavigation<DialogSlotNoteComponent.ErrorDialogNote>()

  init {
    lifecycle.doOnCreate {
      getNotes()
    }
  }

  override fun event(event: NoteRootComponent.Event) = when (event) {
    is NoteRootComponent.Event.GetNotes -> getNotes()
    is NoteRootComponent.Event.Search -> searchItems(event.nameNoteUi)
    is NoteRootComponent.Event.ShowErrorDialog -> showErrorDialog(event.errorDialogUiModel)
    is NoteRootComponent.Event.OnChecked -> updateNote(event.checkedNote)
    is NoteRootComponent.Event.OnOpenUpdateNoteDialog -> showUpdateNoteDialog(event.updateNote)
  }

  private fun updateNote(note: NoteUiModel) {
    scope.launch {
      updateNoteUseCase(note.toNote())
    }
  }

  private fun showErrorDialog(errorDialogUiModel: ErrorDialogUiModel) {
    errorDialogNoteComponentNavigationSlot.activate(DialogSlotNoteComponent.ErrorDialogNote(errorDialogUiModel))
  }

  private fun showUpdateNoteDialog(updateNote: NoteUiModel) {
    updateNoteDialogComponentNavigationSlot.activate(DialogSlotNoteComponent.UpdateNoteDialog(updateNote))
  }

  private fun searchItems(nameNoteUi: NameNoteUi) {
    scope.launch {
      if (nameNoteUi.name.isNotEmpty()) {
        Timber.tag("searchRoutine").d("searchText:${nameNoteUi.name}")
        searchNoteUseCase(nameNoteUi.toNameNote()).catch {
        }.collectLatest { notes ->
          _state.update {
            it.copy(notes = LoadableData.Loaded(notes.map { it.toNoteUiModel() }.toPersistentList()))
          }
        }
      } else {
        getNotes()
      }
    }
  }

  private fun getNotes() {
    scope.launch {
      getNotesUseCase()
        .catch {

        }
        .collectLatest { notes ->
          _state.update {
            it.copy(notes = LoadableData.Loaded(notes.map { it.toNoteUiModel() }.toPersistentList()))
          }
        }
    }
  }

  override val addNoteDialogHomeScreen: Value<ChildSlot<DialogSlotNoteComponent.AddNoteDialog, AddNoteDialogComponent>> =
    childSlot(
      source = addNoteDialogComponentNavigationSlot,
      serializer = DialogSlotNoteComponent.AddNoteDialog.serializer(),
      handleBackButton = true,
      key = "addNoteDialogComponentNavigationSlot",
    ) { config, childComponentContext ->
      AddNoteDialogComponentImpl(
        componentContext = childComponentContext,
        mainDispatcher = Dispatchers.Main,
        ioDispatcher = Dispatchers.IO,
        addNoteUseCase = addNoteUseCase,
        onDismissed = addNoteDialogComponentNavigationSlot::dismiss,
      )
    }

  override val updateNoteDialogScreen: Value<ChildSlot<DialogSlotNoteComponent.UpdateNoteDialog, UpdateNoteDialogComponent>> =
    childSlot(
      source = updateNoteDialogComponentNavigationSlot,
      serializer = DialogSlotNoteComponent.UpdateNoteDialog.serializer(),
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
  override val errorDialogRemoveNoteScreen: Value<ChildSlot<DialogSlotNoteComponent.ErrorDialogNote, ErrorDialogComponent>> =
    childSlot(
      source = errorDialogNoteComponentNavigationSlot,
      serializer = DialogSlotNoteComponent.ErrorDialogNote.serializer(),
      handleBackButton = true,
      key = "errorDialogNoteComponentNavigationSlot",
    ) { config, childComponentContext ->
      ErrorDialogComponentImpl(
        componentContext = childComponentContext,
        mainContext = Dispatchers.Main,
        deleteNoteUseCase = deleteNoteUseCase,
        errorDialogUiModel = config.errorDialogUiModel,
        onDismissed = errorDialogNoteComponentNavigationSlot::dismiss,
      )
    }

  override fun onShowAddNoteDialog() {
    addNoteDialogComponentNavigationSlot.activate(DialogSlotNoteComponent.AddNoteDialog)
  }
}
