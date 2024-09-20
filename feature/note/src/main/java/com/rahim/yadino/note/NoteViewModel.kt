package com.rahim.yadino.note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahim.yadino.base.Constants
import com.rahim.yadino.base.Resource
import com.rahim.yadino.base.calculateTimeFormat
import com.rahim.yadino.base.enums.error.ErrorMessageCode
import com.rahim.yadino.dateTime.DateTimeRepository
import com.rahim.yadino.base.db.model.NoteModel
import com.rahim.yadino.sharedPreferences.SharedPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
  private val noteRepository: NoteRepository,
  private val timeRepository: DateTimeRepository,
  private val sharedPreferencesRepository: SharedPreferencesRepository,
) :
  ViewModel() {
  val currentYear = timeRepository.currentTimeYer
  val currentMonth = timeRepository.currentTimeMonth
  val currentDay = timeRepository.currentTimeDay


  private var _notes = MutableStateFlow<Resource<List<NoteModel>>>(Resource.Loading())
  var notes: StateFlow<Resource<List<NoteModel>>> = _notes

  var nameDay: String? = null

  init {
    getCurrentNameDay()
    getNotes()
  }

  fun addNote(noteModel: NoteModel) {
    viewModelScope.launch {
      noteRepository.addNote(noteModel)
    }
  }

  fun updateNote(noteModel: NoteModel) {
    viewModelScope.launch {
      noteRepository.updateNote(noteModel)
    }
  }

  private fun getCurrentNameDay(
    date: String = String().calculateTimeFormat(
      timeRepository.currentTimeYer,
      timeRepository.currentTimeMonth,
      timeRepository.currentTimeDay.toString(),
    ),
    format: String = Constants.YYYY_MM_DD,
  ) {
    nameDay = timeRepository.getCurrentNameDay(date, format)
  }


  fun delete(noteModel: NoteModel) {
    viewModelScope.launch {
      noteRepository.deleteNote(noteModel)
    }
  }

  fun searchItems(searchText: String) {
    viewModelScope.launch {
      if (searchText.isNotEmpty()) {
        Timber.tag("searchRoutine").d("searchText:$searchText")
        _notes.value = Resource.Loading()
        noteRepository.searchNote(searchText).catch {
          _notes.value = Resource.Error(ErrorMessageCode.ERROR_GET_PROCESS)
        }.collectLatest {
          _notes.value = Resource.Success(it)
        }
      } else {
        getNotes()
      }
    }
  }

  private fun getNotes() {
    viewModelScope.launch {
      _notes.value = Resource.Loading()
      noteRepository.getNotes()
        .catch {
          _notes.value =
            Resource.Error(ErrorMessageCode.ERROR_GET_PROCESS)
        }
        .collectLatest {
          _notes.value = Resource.Success(it)
        }
    }
  }

  fun showSampleNote(isShow: Boolean) {
    viewModelScope.launch {
      sharedPreferencesRepository.isShowSampleNote(isShow)
    }
  }
}
