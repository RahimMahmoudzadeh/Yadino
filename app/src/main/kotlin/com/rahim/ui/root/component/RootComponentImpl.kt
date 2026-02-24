package com.rahim.ui.root.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.arkivanov.essenty.lifecycle.doOnResume
import com.rahim.data.distributionActions.AppDistributionActions
import com.rahim.ui.root.component.RootComponent.ChildStack.HistoryRoutine
import com.rahim.ui.root.component.RootComponent.ChildStack.HomeStack
import com.rahim.ui.root.component.RootComponent.ChildStack.Note
import com.rahim.ui.root.component.RootComponent.ChildStack.OnBoarding
import com.rahim.ui.root.component.RootComponent.ChildStack.Routine
import com.rahim.yadino.core.timeDate.repo.DateTimeRepository
import com.rahim.yadino.core.timeDate.useCase.AddTimeUseCase
import com.rahim.yadino.core.timeDate.useCase.CalculateTodayUseCase
import com.rahim.yadino.home.domain.useCase.AddReminderUseCase
import com.rahim.yadino.home.domain.useCase.CancelReminderUseCase
import com.rahim.yadino.home.domain.useCase.DeleteReminderUseCase
import com.rahim.yadino.home.domain.useCase.GetCurrentDateUseCase
import com.rahim.yadino.home.domain.useCase.GetTodayRoutinesUseCase
import com.rahim.yadino.home.domain.useCase.SearchRoutineUseCase
import com.rahim.yadino.home.domain.useCase.UpdateReminderUseCase
import com.rahim.yadino.home.presentation.ui.root.component.RootHomeComponent
import com.rahim.yadino.home.presentation.ui.root.component.RootHomeComponentImpl
import com.rahim.yadino.navigation.component.DrawerItemType
import com.rahim.yadino.note.domain.useCase.AddNoteUseCase
import com.rahim.yadino.note.domain.useCase.AddSampleNoteUseCase
import com.rahim.yadino.note.domain.useCase.DeleteNoteUseCase
import com.rahim.yadino.note.domain.useCase.GetNotesUseCase
import com.rahim.yadino.note.domain.useCase.SearchNoteUseCase
import com.rahim.yadino.note.domain.useCase.UpdateNoteUseCase
import com.rahim.yadino.note.presentation.ui.root.component.RootNoteComponent
import com.rahim.yadino.note.presentation.ui.root.component.RootNoteComponentImpl
import com.rahim.yadino.onboarding.presentation.ui.component.OnBoardingComponent
import com.rahim.yadino.onboarding.presentation.ui.component.OnBoardingComponentImpl
import com.rahim.yadino.routine.domain.useCase.AddSampleRoutineUseCase
import com.rahim.yadino.routine.domain.useCase.ChangeIdRoutinesUseCase
import com.rahim.yadino.routine.domain.useCase.CheckedAllRoutinePastTimeUseCase
import com.rahim.yadino.routine.domain.useCase.GetAllRoutineUseCase
import com.rahim.yadino.routine.domain.useCase.GetCurrentTimeUseCase
import com.rahim.yadino.routine.domain.useCase.GetRemindersUseCase
import com.rahim.yadino.routine.domain.useCase.GetTimesMonthUseCase
import com.rahim.yadino.routine.domain.useCase.HaveAlarmUseCase
import com.rahim.yadino.routine.presentation.ui.alarmHistory.component.HistoryRoutineComponent
import com.rahim.yadino.routine.presentation.ui.alarmHistory.component.HistoryRoutineComponentImpl
import com.rahim.yadino.routine.presentation.ui.root.component.RootRoutineComponent
import com.rahim.yadino.routine.presentation.ui.root.component.RootRoutineComponentImpl
import com.rahim.yadino.sharedPreferences.repo.SharedPreferencesRepository
import com.rahim.yadino.sharedPreferences.useCase.ChangeThemeUseCase
import com.rahim.yadino.sharedPreferences.useCase.IsDarkThemeUseCase
import com.rahim.yadino.sharedPreferences.useCase.IsShowWelcomeScreenUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class RootComponentImpl(componentContext: ComponentContext) : RootComponent, ComponentContext by componentContext, KoinComponent {
  private val navigation = StackNavigation<RootComponent.ChildConfig>()

  private val addReminderUseCase: AddReminderUseCase = get()
  private val updateReminderUseCase: UpdateReminderUseCase = get()
  private val cancelReminderUseCase: CancelReminderUseCase = get()
  private val deleteReminderUseCase: DeleteReminderUseCase = get()
  private val getTodayRoutinesUseCase: GetTodayRoutinesUseCase = get()
  private val searchRoutineUseCase: SearchRoutineUseCase = get()
  private val getCurrentDateUseCase: GetCurrentDateUseCase = get()

  private val sharedPreferencesRepository: SharedPreferencesRepository = get()

  override val stack: Value<ChildStack<*, RootComponent.ChildStack>> = childStack(
    source = navigation,
    serializer = RootComponent.ChildConfig.serializer(),
    initialConfiguration = RootComponent.ChildConfig.OnBoarding,
    handleBackButton = true,
    childFactory = ::childComponent,
  )

  override fun onTabClick(tab: RootComponent.ChildConfig) {
    navigation.bringToFront(tab)
  }

  @OptIn(DelicateDecomposeApi::class)
  override fun showHistoryRoutine() {
    navigation.push(RootComponent.ChildConfig.HistoryRoutine)
  }

  override fun navigateUp() {
    navigation.pop()
  }

  private fun homeComponent(componentContext: ComponentContext): RootHomeComponent = RootHomeComponentImpl(
    componentContext = componentContext,
    mainContext = Dispatchers.Main,
    updateReminderUseCase = updateReminderUseCase,
    cancelReminderUseCase = cancelReminderUseCase,
    getTodayRoutinesUseCase = getTodayRoutinesUseCase,
    searchRoutineUseCase = searchRoutineUseCase,
    getCurrentDateUseCase = getCurrentDateUseCase,
    deleteReminderUseCase = deleteReminderUseCase,
    addReminderUseCase = addReminderUseCase,
  )

  private fun onBoardingComponent(componentContext: ComponentContext): OnBoardingComponent = OnBoardingComponentImpl(
    componentContext = componentContext,
    mainContext = Dispatchers.Main,
    sharedPreferencesRepository = sharedPreferencesRepository,
    onNavigateToHome = {
      navigation.replaceAll(RootComponent.ChildConfig.Home)
    },
  )

  private val getAllRoutineUseCase: GetAllRoutineUseCase = get()
  private fun historyRoutineComponent(componentContext: ComponentContext): HistoryRoutineComponent = HistoryRoutineComponentImpl(
    componentContext = componentContext,
    mainContext = Dispatchers.Main,
    getAllRoutineUseCase = getAllRoutineUseCase,
  )

  private val addReminderUseCaseRoutine: com.rahim.yadino.routine.domain.useCase.AddReminderUseCase = get()
  private val cancelReminderUseCaseRoutine: com.rahim.yadino.routine.domain.useCase.CancelReminderUseCase = get()
  private val getTodayRoutinesUseCaseRoutine: GetRemindersUseCase = get()
  private val searchRoutineUseCaseRoutine: com.rahim.yadino.routine.domain.useCase.SearchRoutineUseCase = get()
  private val dateTimeRepository: DateTimeRepository = get()
  private val getTimesMonthUseCase: GetTimesMonthUseCase = get()
  private val getCurrentTimeUseCase: GetCurrentTimeUseCase = get()
  private val updateReminderUseCaseRoutine: com.rahim.yadino.routine.domain.useCase.UpdateReminderUseCase = get()
  private val deleteReminderUseCaseRoutine: com.rahim.yadino.routine.domain.useCase.DeleteReminderUseCase = get()


  private fun routineComponent(componentContext: ComponentContext): RootRoutineComponent = RootRoutineComponentImpl(
    componentContext = componentContext,
    mainContext = Dispatchers.Main,
    ioContext = Dispatchers.IO,
    cancelReminderUseCase = cancelReminderUseCaseRoutine,
    getRemindersUseCase = getTodayRoutinesUseCaseRoutine,
    searchRoutineUseCase = searchRoutineUseCaseRoutine,
    dateTimeRepository = dateTimeRepository,
    updateReminderUseCase = updateReminderUseCaseRoutine,
    deleteReminderUseCase = deleteReminderUseCaseRoutine,
    addReminderUseCase = addReminderUseCaseRoutine,
    getTimesMonthUseCase = getTimesMonthUseCase,
    getCurrentTimeUseCase = getCurrentTimeUseCase,
  )

  private val deleteNoteUseCase: DeleteNoteUseCase = get()
  private val getNotesUseCase: GetNotesUseCase = get()
  private val searchNoteUseCase: SearchNoteUseCase = get()
  private val addNoteUseCase: AddNoteUseCase = get()
  private val updateNoteUseCase: UpdateNoteUseCase = get()

  private fun noteComponent(componentContext: ComponentContext): RootNoteComponent = RootNoteComponentImpl(
    componentContext = componentContext,
    mainContext = Dispatchers.Main,
    ioContext = Dispatchers.IO,
    getNotesUseCase = getNotesUseCase,
    searchNoteUseCase = searchNoteUseCase,
    updateNoteUseCase = updateNoteUseCase,
    deleteNoteUseCase = deleteNoteUseCase,
    addNoteUseCase = addNoteUseCase,
  )

  private fun childComponent(
    config: RootComponent.ChildConfig,
    childComponentContext: ComponentContext,
  ): RootComponent.ChildStack = when (config) {
    RootComponent.ChildConfig.Home -> HomeStack(component = homeComponent(componentContext = childComponentContext))
    RootComponent.ChildConfig.OnBoarding -> OnBoarding(component = onBoardingComponent(componentContext = childComponentContext))
    RootComponent.ChildConfig.HistoryRoutine -> HistoryRoutine(component = historyRoutineComponent(componentContext = childComponentContext))
    RootComponent.ChildConfig.Note -> Note(component = noteComponent(componentContext = childComponentContext))
    RootComponent.ChildConfig.Routine -> Routine(component = routineComponent(componentContext = childComponentContext))
  }


  private val mutableState = MutableValue(RootComponent.State())
  override val state: Value<RootComponent.State> = mutableState

  private val calculateTodayUseCase: CalculateTodayUseCase = get()
  private val addTimeUseCase: AddTimeUseCase = get()
  private val addSampleNoteUseCase: AddSampleNoteUseCase = get()
  private val addSampleRoutineUseCase: AddSampleRoutineUseCase = get()
  private val changeIdRoutinesUseCase: ChangeIdRoutinesUseCase = get()
  private val haveAlarmUseCase: HaveAlarmUseCase = get()
  private val checkedAllRoutinePastTimeUseCase: CheckedAllRoutinePastTimeUseCase = get()
  private val changeThemeUseCase: ChangeThemeUseCase = get()
  private val isDarkThemeUseCase: IsDarkThemeUseCase = get()
  private val isShowWelcomeScreenUseCase: IsShowWelcomeScreenUseCase = get()
  private val appDistributionActions: AppDistributionActions = get()

  override fun onEvent(event: RootComponent.Event) = when (event) {
    is RootComponent.Event.ClickDrawer -> clickDrawerItem(event.drawerItemType)
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
  private val scopeMain: CoroutineScope = coroutineScope(Dispatchers.Main + SupervisorJob())
  private val scopeIo: CoroutineScope = coroutineScope(Dispatchers.IO + SupervisorJob())

  init {
    lifecycle.run {
      this.doOnResume {
        checkedAllRoutinePastTime()
      }
      this.doOnCreate {
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
