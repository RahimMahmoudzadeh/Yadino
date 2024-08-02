package com.rahim.ui.main

import androidx.lifecycle.viewModelScope
import com.rahim.yadino.base.viewmodel.BaseViewModel
import com.rahim.yadino.dateTime.DataTimeRepository
import com.rahim.yadino.note.NoteRepository
import com.rahim.yadino.routine.RepositoryRoutine
import com.rahim.yadino.sharedPreferences.SharedPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataTimeRepository: DataTimeRepository,
    private val repositoryRoutine: RepositoryRoutine,
    private val noteRepository: NoteRepository,
    @com.rahim.yadino.base.di.IODispatcher
    private val ioDispatcher: CoroutineDispatcher,
    private val sharedPreferencesRepository: SharedPreferencesRepository
) :
    BaseViewModel() {
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

    fun isShowWelcomeScreen() = sharedPreferencesRepository.isShowWelcomeScreen()

    fun showSampleRoutine(isShow: Boolean = true) {
        viewModelScope.launch {
            sharedPreferencesRepository.isShowSampleRoutine(isShow)
        }
    }
}