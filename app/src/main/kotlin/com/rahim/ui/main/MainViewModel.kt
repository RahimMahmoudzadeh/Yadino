package com.rahim.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahim.data.distributionActions.AppDistributionActions
import com.rahim.yadino.core.timeDate.repo.DateTimeRepository
import com.rahim.yadino.di.IODispatcher
import com.rahim.yadino.navigation.component.DrawerItemType
import com.rahim.yadino.note.domain.NoteRepository
import com.rahim.yadino.sharedPreferences.repo.SharedPreferencesRepository
import com.yadino.routine.domain.repo.RoutineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
  private val dateTimeRepository: DateTimeRepository,
  private val repositoryRoutine: RoutineRepository,
  private val noteRepository: NoteRepository,
  private val appDistributionActions: AppDistributionActions,
  @IODispatcher
  private val ioDispatcher: CoroutineDispatcher,
  private val sharedPreferencesRepository: SharedPreferencesRepository,
) :
  ViewModel(), MainContract {

  private val mutableState = MutableStateFlow(MainContract.MainState())
  override val state: StateFlow<MainContract.MainState> = mutableState.onStart {
    initialize()
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), MainContract.MainState())

  override fun event(event: MainContract.MainEvent) = when (event) {
    is MainContract.MainEvent.CheckedAllRoutinePastTime -> {
      checkedAllRoutinePastTime()
    }

    is MainContract.MainEvent.ClickDrawer -> clickDrawerItem(event.drawerItemType)
  }

  private fun clickDrawerItem(drawerItemType: DrawerItemType) {
    when (drawerItemType) {
      is DrawerItemType.RateToApp -> {
        val state = appDistributionActions.drawerItemType(com.rahim.data.distributionActions.DrawerItemType.RateToApp)
        mutableState.update {
          it.copy(stateOfClickItemDrawable = state)
        }
      }
      is DrawerItemType.ShareWithFriends -> {
        val state = appDistributionActions.drawerItemType(com.rahim.data.distributionActions.DrawerItemType.ShareWithFriends)
        mutableState.update {
          it.copy(stateOfClickItemDrawable = state)
        }
      }
      is DrawerItemType.Theme -> {
        setDarkTheme(state.value.isDarkTheme != true)
      }
    }
  }

  private fun initialize() {
    viewModelScope.launch(ioDispatcher) {
      launch {
        dateTimeRepository.calculateToday()
      }
      launch {
        dateTimeRepository.addTime()
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
      launch {
        isShowWelcomeScreen()
      }
      launch {
        haveAlarm()
      }
      launch {
        getDarkTheme()
      }
    }
  }

  private suspend fun haveAlarm() {
    repositoryRoutine.haveAlarm().catch {}.collect { haveAlarm ->
      mutableState.update {
        it.copy(haveAlarm = haveAlarm)
      }
    }
  }

  private fun checkedAllRoutinePastTime() {
    viewModelScope.launch {
      repositoryRoutine.checkedAllRoutinePastTime()
    }
  }

  private fun setDarkTheme(isDarkTheme: Boolean) {
    viewModelScope.launch {
      sharedPreferencesRepository.changeTheme(isDarkTheme)
    }
  }

  private suspend fun getDarkTheme() {
    sharedPreferencesRepository.isDarkTheme().catch {}.collect { theme ->
      mutableState.update {
        it.copy(isDarkTheme = theme)
      }
    }
  }

  private suspend fun isShowWelcomeScreen() {
    sharedPreferencesRepository.isShowWelcomeScreen().catch {}.collect { isShow ->
      mutableState.update {
        it.copy(isShowWelcomeScreen = isShow)
      }
    }
  }
}
