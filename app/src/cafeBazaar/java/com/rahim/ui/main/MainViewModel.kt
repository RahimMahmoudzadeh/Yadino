package com.rahim.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahim.data.repository.base.BaseRepository
import com.rahim.data.repository.dataTime.DataTimeRepository
import com.rahim.data.repository.note.NoteRepository
import com.rahim.data.repository.routine.RepositoryRoutine
import com.rahim.data.repository.sharedPreferences.SharedPreferencesRepository
import com.rahim.data.sharedPreferences.SharedPreferencesCustom
import com.rahim.utils.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataTimeRepository: DataTimeRepository,
    private val repositoryRoutine: RepositoryRoutine,
    private val noteRepository: NoteRepository,
    baseRepository: BaseRepository,
    sharedPreferencesRepository: SharedPreferencesRepository
) :
    BaseViewModel(sharedPreferencesRepository, baseRepository) {
    init {
        viewModelScope.launch {
            launch {
                dataTimeRepository.addTime()
            }
            launch {
                repositoryRoutine.addSampleRoutine()
            }
            launch {
                noteRepository.addSampleNote()
            }
            launch {
                repositoryRoutine.changeRoutineId()
            }
        }
    }
}