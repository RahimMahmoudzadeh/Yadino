package com.rahim.yadino.note.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahim.yadino.base.LoadableData
import com.rahim.yadino.note.domain.NoteRepository
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class NoteViewModel(
  private val addNoteUseCase: AddNoteUseCase,
  private val deleteNoteUseCase: DeleteNoteUseCase,
  private val updateNoteUseCase: UpdateNoteUseCase,
  private val getNotesUseCase: GetNotesUseCase,
  private val searchNoteUseCase: SearchNoteUseCase,
) :
  ViewModel(), NoteContract {

  private var mutableState = MutableStateFlow(NoteContract.NoteState())
  override val state: StateFlow<NoteContract.NoteState> = mutableState.onStart {
//    getCurrentNameDay()
    getNotes()
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NoteContract.NoteState())

  override fun event(event: NoteContract.NoteEvent) = when (event) {
    is NoteContract.NoteEvent.GetNotes -> getNotes()
    is NoteContract.NoteEvent.SearchNote -> searchItems(event.nameNoteUi)
    is NoteContract.NoteEvent.DeleteNote -> delete(event.deleteNote)
    is NoteContract.NoteEvent.UpdateNote -> updateNote(event.updateNote)
    is NoteContract.NoteEvent.AddNote -> addNote(event.addNote)
  }

  private fun addNote(note: NoteUiModel) {
    viewModelScope.launch {
      addNoteUseCase(note.toNote())
    }
  }

  private fun updateNote(note: NoteUiModel) {
    viewModelScope.launch {
      updateNoteUseCase(note.toNote())
    }
  }

  private fun delete(note: NoteUiModel) {
    viewModelScope.launch {
      deleteNoteUseCase(note.toNote())
    }
  }

  private fun searchItems(nameNoteUi: NameNoteUi) {
    viewModelScope.launch {
      if (nameNoteUi.name.isNotEmpty()) {
        Timber.tag("searchRoutine").d("searchText:${nameNoteUi.name}")
        searchNoteUseCase(nameNoteUi.toNameNote()).catch {
//          mutableState.update {
//            it.copy(errorMessage = ErrorMessageCode.ERROR_GET_PROCESS)
//          }
        }.collectLatest { notes ->
          mutableState.update {
            it.copy(notes = LoadableData.Loaded(notes.map { it.toNoteUiModel() }.toPersistentList()))
          }
        }
      } else {
        getNotes()
      }
    }
  }

  private fun getNotes() {
    viewModelScope.launch {
      getNotesUseCase()
        .catch {
//          mutableState.update {
//            it.copy(errorMessage = ErrorMessageCode.ERROR_GET_PROCESS)
//          }
        }
        .collectLatest { notes ->
          mutableState.update {
            it.copy(notes = LoadableData.Loaded(notes.map { it.toNoteUiModel() }.toPersistentList()))
          }
        }
    }
  }
}
