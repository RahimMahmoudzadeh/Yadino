package com.rahim.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahim.yadino.dateTime.DateTimeRepository
import com.rahim.yadino.di.IODispatcher
import com.rahim.yadino.note.NoteRepository
import com.rahim.yadino.routine.RepositoryRoutine
import com.rahim.yadino.sharedPreferences.SharedPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
  private val dateTimeRepository: DateTimeRepository,
  private val repositoryRoutine: RepositoryRoutine,
  private val noteRepository: NoteRepository,
  @IODispatcher
  private val ioDispatcher: CoroutineDispatcher,
  private val sharedPreferencesRepository: SharedPreferencesRepository
) :
  ViewModel() {
  val haveAlarm : Flow<Boolean> = repositoryRoutine.haveAlarm()
  init {
    viewModelScope.launch(ioDispatcher) {
      launch {
        dateTimeRepository.calculateToday()
      }
      launch {
        dateTimeRepository.addTime()
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
      repositoryRoutine.checkedAllRoutinePastTime()
    }
  }

  fun setDarkTheme(isDarkTheme: String) {
    viewModelScope.launch {
      sharedPreferencesRepository.changeTheme(isDarkTheme)
    }
  }

  fun isDarkTheme() = sharedPreferencesRepository.isDarkTheme()

  fun isShowWelcomeScreen() = sharedPreferencesRepository.isShowWelcomeScreen()
}
