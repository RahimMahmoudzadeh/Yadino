package com.rahim.ui.note

import androidx.lifecycle.viewModelScope
import com.rahim.data.modle.note.NoteModel
import com.rahim.data.repository.base.BaseRepository
import com.rahim.data.repository.dataTime.DataTimeRepository
import com.rahim.data.repository.note.NoteRepository
import com.rahim.data.repository.sharedPreferences.SharedPreferencesRepository
import com.rahim.utils.Constants
import com.rahim.utils.base.viewModel.BaseViewModel
import com.rahim.utils.enums.error.ErrorMessageCode
import com.rahim.utils.extention.calculateTimeFormat
import com.rahim.utils.resours.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    baseRepository: BaseRepository,
    private val noteRepository: NoteRepository,
    private val timeRepository: DataTimeRepository,
    private val sharedPreferencesRepository: SharedPreferencesRepository,
) :
    BaseViewModel(sharedPreferencesRepository, baseRepository) {

    private var _notes = MutableStateFlow<Resource<List<NoteModel>>>(Resource.Loading())
    var notes: StateFlow<Resource<List<NoteModel>>> = _notes.asStateFlow()

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
            currentYear,
            currentMonth,
            currentDay.toString()
        ), format: String = Constants.YYYY_MM_DD
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