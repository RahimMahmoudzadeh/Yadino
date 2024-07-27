package com.rahim.ui.main

import androidx.lifecycle.viewModelScope
import com.rahim.yadino.base.di.IODispatcher
import com.rahim.data.repository.base.BaseRepository
import com.rahim.data.repository.dataTime.DataTimeRepository
import com.rahim.data.repository.note.NoteRepository
import com.rahim.yadino.routine.RepositoryRoutine
import com.rahim.data.repository.sharedPreferences.SharedPreferencesRepository
import com.rahim.yadino.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataTimeRepository: DataTimeRepository,
    private val repositoryRoutine: com.rahim.yadino.routine.RepositoryRoutine,
    private val noteRepository: NoteRepository,
    @com.rahim.yadino.base.di.IODispatcher
    private val ioDispatcher: CoroutineDispatcher,
    baseRepository: BaseRepository,
    sharedPreferencesRepository: SharedPreferencesRepository
) :
    com.rahim.yadino.base.viewmodel.BaseViewModel(sharedPreferencesRepository, baseRepository) {
    init {
        viewModelScope.launch(ioDispatcher) {
            launch {
                dataTimeRepository.calculateToday()
            }
            launch {
                dataTimeRepository.addTime()
            }
            launch {
                Timber.tag("sampleRoutines").d("mainViewModel")
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

    fun checkedAllRoutinePastTime() {
        viewModelScope.launch {
            repositoryRoutine.checkEdAllRoutinePastTime()
        }
    }

    fun setDarkTheme(isDarkTheme: String) {
        sharedPreferencesRepository.changeTheme(isDarkTheme)
    }

    fun isDarkTheme() = sharedPreferencesRepository.isDarkTheme()
}