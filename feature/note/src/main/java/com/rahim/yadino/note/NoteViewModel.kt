package com.rahim.yadino.note

import androidx.lifecycle.viewModelScope
import com.rahim.yadino.Constants
import com.rahim.yadino.base.BaseViewModel
import com.rahim.yadino.calculateTimeFormat
import com.rahim.yadino.enums.error.ErrorMessageCode
import com.rahim.yadino.dateTime.DateTimeRepository
import com.rahim.yadino.model.NoteModel
import com.rahim.yadino.sharedPreferences.SharedPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
  private val noteRepository: NoteRepository,
  private val timeRepository: DateTimeRepository,
) :
  BaseViewModel(), NoteContract {
  private val currentYear = timeRepository.currentTimeYear
  private val currentMonth = timeRepository.currentTimeMonth
  private val currentDay = timeRepository.currentTimeDay


  private var mutableState = MutableStateFlow<NoteContract.NoteState>(NoteContract.NoteState())
  override val state: StateFlow<NoteContract.NoteState> = mutableState.onStart {
    getCurrentNameDay()
    getNotes()
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NoteContract.NoteState())

  override fun event(event: NoteContract.NoteEvent) = when (event) {
    is NoteContract.NoteEvent.GetNotes -> getNotes()
    is NoteContract.NoteEvent.SearchNote -> searchItems(event.searchText)
    is NoteContract.NoteEvent.DeleteNote -> delete(event.deleteNote)
    is NoteContract.NoteEvent.UpdateNote -> updateNote(event.updateNote)
    is NoteContract.NoteEvent.AddNote -> addNote(event.addNote)
  }

  private fun addNote(noteModel: NoteModel) {
    viewModelScope.launch {
      val updateNote=noteModel.copy(dayNumber = currentDay, monthNumber = currentMonth, yearNumber = currentYear)
      noteRepository.addNote(updateNote)
    }
  }

  private fun updateNote(noteModel: NoteModel) {
    viewModelScope.launch {
      noteRepository.updateNote(noteModel)
    }
  }

  private fun getCurrentNameDay(
    date: String = String().calculateTimeFormat(
      timeRepository.currentTimeYear,
      timeRepository.currentTimeMonth,
      timeRepository.currentTimeDay.toString(),
    ),
    format: String = Constants.YYYY_MM_DD,
  ) {
    mutableState.update {
      it.copy(nameDay = timeRepository.getCurrentNameDay(date, format))
    }
  }


  private fun delete(noteModel: NoteModel) {
    viewModelScope.launch {
      noteRepository.deleteNote(noteModel)
    }
  }

  private fun searchItems(searchText: String) {
    viewModelScope.launch {
      if (searchText.isNotEmpty()) {
        Timber.tag("searchRoutine").d("searchText:$searchText")
        noteRepository.searchNote(searchText).catch {
          mutableState.update {
            it.copy(errorMessage = ErrorMessageCode.ERROR_GET_PROCESS)
          }
        }.collectLatest { notes ->
          mutableState.update {
            it.copy(notes = notes, errorMessage = null, isLoading = false)
          }
        }
      } else {
        getNotes()
      }
    }
  }

  private fun getNotes() {
    viewModelScope.launch {
      noteRepository.getNotes()
        .catch {
          mutableState.update {
            it.copy(errorMessage = ErrorMessageCode.ERROR_GET_PROCESS)
          }
        }
        .collectLatest { notes ->
          mutableState.update {
            it.copy(notes = notes, errorMessage = null, isLoading = false)
          }
        }
    }
  }
}
