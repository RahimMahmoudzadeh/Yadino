package com.rahim.yadino.note.presentation.component

import com.arkivanov.decompose.ComponentContext
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
import com.rahim.yadino.note.presentation.model.NameNoteUi
import com.rahim.yadino.note.presentation.model.NoteUiModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class NoteComponentImpl(
  componentContext: ComponentContext,
  mainContext: CoroutineContext,
  private val deleteNoteUseCase: DeleteNoteUseCase,
  private val getNotesUseCase: GetNotesUseCase,
  private val searchNoteUseCase: SearchNoteUseCase,
) : NoteComponent, ComponentContext by componentContext {

  private val scope: CoroutineScope = coroutineScope(mainContext + SupervisorJob())

  private var _state = MutableValue(NoteComponent.NoteState())
  override val state: Value<NoteComponent.NoteState> = _state

  init {
    lifecycle.doOnCreate {
      getNotes()
    }
  }

  override fun event(event: NoteComponent.NoteEvent) = when (event) {
    is NoteComponent.NoteEvent.GetNotes -> getNotes()
    is NoteComponent.NoteEvent.SearchNote -> searchItems(event.nameNoteUi)
    is NoteComponent.NoteEvent.DeleteNote -> delete(event.deleteNote)
    is NoteComponent.NoteEvent.OnCheckedNote -> TODO()
  }

  private fun delete(note: NoteUiModel) {
    scope.launch {
      deleteNoteUseCase(note.toNote())
    }
  }

  private fun searchItems(nameNoteUi: NameNoteUi) {
    scope.launch {
      if (nameNoteUi.name.isNotEmpty()) {
        Timber.tag("searchRoutine").d("searchText:${nameNoteUi.name}")
        searchNoteUseCase(nameNoteUi.toNameNote()).catch {
//          mutableState.update {
//            it.copy(errorMessage = ErrorMessageCode.ERROR_GET_PROCESS)
//          }
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
//          mutableState.update {
//            it.copy(errorMessage = ErrorMessageCode.ERROR_GET_PROCESS)
//          }
        }
        .collectLatest { notes ->
          _state.update {
            it.copy(notes = LoadableData.Loaded(notes.map { it.toNoteUiModel() }.toPersistentList()))
          }
        }
    }
  }
}
