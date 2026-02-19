package com.rahim.ui.main.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.rahim.data.distributionActions.AppDistributionActions
import com.rahim.yadino.core.timeDate.repo.DateTimeRepository
import com.rahim.yadino.core.timeDate.useCase.AddTimeUseCase
import com.rahim.yadino.core.timeDate.useCase.CalculateTodayUseCase
import com.rahim.yadino.navigation.component.DrawerItemType
import com.rahim.yadino.note.domain.NoteRepository
import com.rahim.yadino.note.domain.useCase.AddSampleNoteUseCase
import com.rahim.yadino.routine.domain.repo.RoutineRepository
import com.rahim.yadino.routine.domain.useCase.AddSampleRoutineUseCase
import com.rahim.yadino.routine.domain.useCase.ChangeIdRoutinesUseCase
import com.rahim.yadino.routine.domain.useCase.CheckedAllRoutinePastTimeUseCase
import com.rahim.yadino.routine.domain.useCase.HaveAlarmUseCase
import com.rahim.yadino.sharedPreferences.repo.SharedPreferencesRepository
import com.rahim.yadino.sharedPreferences.useCase.ChangeThemeUseCase
import com.rahim.yadino.sharedPreferences.useCase.IsDarkThemeUseCase
import com.rahim.yadino.sharedPreferences.useCase.IsShowWelcomeScreenUseCase
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
  private val calculateTodayUseCase: CalculateTodayUseCase,
  private val addTimeUseCase: AddTimeUseCase,
  private val addSampleNoteUseCase: AddSampleNoteUseCase,
  private val addSampleRoutineUseCase: AddSampleRoutineUseCase,
  private val changeIdRoutinesUseCase: ChangeIdRoutinesUseCase,
  private val haveAlarmUseCase: HaveAlarmUseCase,
  private val checkedAllRoutinePastTimeUseCase: CheckedAllRoutinePastTimeUseCase,
  private val changeThemeUseCase: ChangeThemeUseCase,
  private val isDarkThemeUseCase: IsDarkThemeUseCase,
  private val isShowWelcomeScreenUseCase: IsShowWelcomeScreenUseCase,
  private val appDistributionActions: AppDistributionActions,
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
          calculateTodayUseCase()
        }
        launch {
          addTimeUseCase()
        }
        launch {
          addSampleRoutineUseCase()
        }
        launch {
          addSampleNoteUseCase()
        }
        launch {
          changeIdRoutinesUseCase()
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
    haveAlarmUseCase().catch {}.collect { haveAlarm ->
      mutableState.update {
        it.copy(haveAlarm = haveAlarm)
      }
    }
  }

  private fun checkedAllRoutinePastTime() {
    scopeMain.launch {
      checkedAllRoutinePastTimeUseCase()
    }
  }

  private fun setDarkTheme(isDarkTheme: Boolean) {
    scopeMain.launch {
      changeThemeUseCase(isDarkTheme)
    }
  }

  private suspend fun getDarkTheme() {
    isDarkThemeUseCase().catch {}.collect { theme ->
      mutableState.update {
        it.copy(isDarkTheme = theme)
      }
    }
  }

  private suspend fun isShowWelcomeScreen() {
    isShowWelcomeScreenUseCase().catch {}.collect { isShow ->
      mutableState.update {
        it.copy(isShowWelcomeScreen = isShow)
      }
    }
  }
}
