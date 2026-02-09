package com.rahim.yadino.note.presentation.ui.root.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.rahim.yadino.base.LoadableData
import com.rahim.yadino.note.domain.useCase.GetNotesUseCase
import com.rahim.yadino.note.domain.useCase.SearchNoteUseCase
import com.rahim.yadino.note.domain.useCase.UpdateNoteUseCase
import com.rahim.yadino.note.presentation.mapper.toNameNote
import com.rahim.yadino.note.presentation.mapper.toNote
import com.rahim.yadino.note.presentation.mapper.toNoteUiModel
import com.rahim.yadino.note.presentation.model.ErrorDialogUiModel
import com.rahim.yadino.note.presentation.model.NameNoteUi
import com.rahim.yadino.note.presentation.model.NoteUiModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineScope
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
  private val updateNoteUseCase: UpdateNoteUseCase,
  private val onOpenUpdateNoteDialog: (updateNote: NoteUiModel) -> Unit,
  private val onShowErrorDialog: (errorDialogUiModel: ErrorDialogUiModel) -> Unit,
) : NoteRootComponent, ComponentContext by componentContext {

  private val scope: CoroutineScope = coroutineScope(mainContext + SupervisorJob())

  private var _state = MutableValue(NoteRootComponent.State())
  override val state: Value<NoteRootComponent.State> = _state

  private val _effect: Channel<NoteRootComponent.Effect> = Channel(Channel.BUFFERED)
  override val effect: Flow<NoteRootComponent.Effect> = _effect.consumeAsFlow()


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
    is NoteRootComponent.Event.OnOpenUpdateNoteDialog -> onOpenUpdateNoteDialog(event.updateNote)
  }

  private fun updateNote(note: NoteUiModel){
    scope.launch {
      updateNoteUseCase(note.toNote())
    }
  }

  private fun showErrorDialog(errorDialogUiModel: ErrorDialogUiModel) {
    onShowErrorDialog(errorDialogUiModel)
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
}
