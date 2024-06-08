package com.rahim.ui.note

import androidx.lifecycle.viewModelScope
import com.rahim.data.modle.note.NoteModel
import com.rahim.data.repository.base.BaseRepository
import com.rahim.data.repository.note.NoteRepository
import com.rahim.data.repository.sharedPreferences.SharedPreferencesRepository
import com.rahim.utils.base.viewModel.BaseViewModel
import com.rahim.utils.enums.error.ErrorMessageCode
import com.rahim.utils.resours.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    sharedPreferencesRepository: SharedPreferencesRepository,
    baseRepository: BaseRepository,
    private val noteRepository: NoteRepository
) :
    BaseViewModel(sharedPreferencesRepository, baseRepository) {

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

    fun getNotes(): Flow<Resource<List<NoteModel>>> = flow {
        emit(Resource.Loading())
        noteRepository.getNotes().catch {
            emit(Resource.Error(ErrorMessageCode.ERROR_GET_PROCESS))
        }.collect {
            emit(Resource.Success(it.sortedBy {
                it.timeInMileSecond?.let {
                    it
                }
            }))

        }
    }

    fun delete(noteModel: NoteModel) {
        viewModelScope.launch {
            noteRepository.deleteNote(noteModel)
        }
    }
}