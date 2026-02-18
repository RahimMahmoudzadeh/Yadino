package com.rahim.ui.main

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.rahim.data.distributionActions.AppDistributionActions
import com.rahim.yadino.core.timeDate.repo.DateTimeRepository
import com.rahim.yadino.navigation.component.DrawerItemType
import com.rahim.yadino.note.domain.NoteRepository
import com.rahim.yadino.routine.domain.repo.RoutineRepository
import com.rahim.yadino.sharedPreferences.repo.SharedPreferencesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MainComponentImpl(
  mainContext: CoroutineContext,
  ioContext: CoroutineDispatcher,
  componentContext: ComponentContext,
  private val dateTimeRepository: DateTimeRepository,
  private val repositoryRoutine: RoutineRepository,
  private val noteRepository: NoteRepository,
  private val appDistributionActions: AppDistributionActions,
  private val sharedPreferencesRepository: SharedPreferencesRepository,
) : MainComponent, ComponentContext by componentContext {

  private val scopeMain: CoroutineScope = coroutineScope(mainContext + SupervisorJob())
  private val scopeIo: CoroutineScope = coroutineScope(ioContext + SupervisorJob())

  private val mutableState = MutableValue(MainComponent.MainState())
  override val state: Value<MainComponent.MainState> = mutableState

  override fun onEvent(event: MainComponent.MainEvent) = when (event) {
    is MainComponent.MainEvent.CheckedAllRoutinePastTime -> {
      checkedAllRoutinePastTime()
    }

    is MainComponent.MainEvent.ClickDrawer -> clickDrawerItem(event.drawerItemType)
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

  init {
    lifecycle.doOnCreate {
      scopeIo.launch {
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
  }

  private suspend fun haveAlarm() {
    repositoryRoutine.haveAlarm().catch {}.collect { haveAlarm ->
      mutableState.update {
        it.copy(haveAlarm = haveAlarm)
      }
    }
  }

  private fun checkedAllRoutinePastTime() {
    scopeMain.launch {
      repositoryRoutine.checkedAllRoutinePastTime()
    }
  }

  private fun setDarkTheme(isDarkTheme: Boolean) {
    scopeMain.launch {
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
