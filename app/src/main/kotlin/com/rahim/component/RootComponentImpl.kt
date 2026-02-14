package com.rahim.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import com.rahim.component.RootComponent.ChildStack.HistoryRoutine
import com.rahim.component.RootComponent.ChildStack.HomeStack
import com.rahim.component.RootComponent.ChildStack.Note
import com.rahim.component.RootComponent.ChildStack.OnBoarding
import com.rahim.component.RootComponent.ChildStack.Routine
import com.rahim.component.config.ConfigChildComponent
import com.rahim.yadino.core.timeDate.repo.DateTimeRepository
import com.rahim.yadino.home.domain.useCase.AddReminderUseCase
import com.rahim.yadino.home.domain.useCase.CancelReminderUseCase
import com.rahim.yadino.home.domain.useCase.DeleteReminderUseCase
import com.rahim.yadino.home.domain.useCase.GetCurrentDateUseCase
import com.rahim.yadino.home.domain.useCase.GetTodayRoutinesUseCase
import com.rahim.yadino.home.domain.useCase.SearchRoutineUseCase
import com.rahim.yadino.home.domain.useCase.UpdateReminderUseCase
import com.rahim.yadino.home.presentation.ui.root.component.RootHomeComponent
import com.rahim.yadino.home.presentation.ui.root.component.RootHomeComponentImpl
import com.rahim.yadino.note.domain.useCase.AddNoteUseCase
import com.rahim.yadino.note.domain.useCase.DeleteNoteUseCase
import com.rahim.yadino.note.domain.useCase.GetNotesUseCase
import com.rahim.yadino.note.domain.useCase.SearchNoteUseCase
import com.rahim.yadino.note.domain.useCase.UpdateNoteUseCase
import com.rahim.yadino.note.presentation.ui.root.component.RootNoteComponent
import com.rahim.yadino.note.presentation.ui.root.component.RootNoteComponentImpl
import com.rahim.yadino.onboarding.presentation.component.OnBoardingComponent
import com.rahim.yadino.onboarding.presentation.component.OnBoardingComponentImpl
import com.rahim.yadino.routine.domain.useCase.GetAllRoutineUseCase
import com.rahim.yadino.routine.domain.useCase.GetCurrentTimeUseCase
import com.rahim.yadino.routine.domain.useCase.GetRemindersUseCase
import com.rahim.yadino.routine.domain.useCase.GetTimesMonthUseCase
import com.rahim.yadino.routine.presentation.ui.alarmHistory.component.HistoryRoutineComponent
import com.rahim.yadino.routine.presentation.ui.alarmHistory.component.HistoryRoutineComponentImpl
import com.rahim.yadino.routine.presentation.ui.root.component.RootRoutineComponent
import com.rahim.yadino.routine.presentation.ui.root.component.RootRoutineComponentImpl
import com.rahim.yadino.sharedPreferences.repo.SharedPreferencesRepository
import kotlinx.coroutines.Dispatchers
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class RootComponentImpl(componentContext: ComponentContext) : RootComponent, ComponentContext by componentContext, KoinComponent {
  private val navigation = StackNavigation<ConfigChildComponent>()

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
    serializer = ConfigChildComponent.serializer(),
    initialConfiguration = ConfigChildComponent.OnBoarding,
    handleBackButton = true,
    childFactory = ::childComponent,
  )

  override fun onTabClick(tab: ConfigChildComponent) {
    navigation.bringToFront(tab)
  }

  @OptIn(DelicateDecomposeApi::class)
  override fun showHistoryRoutine() {
    navigation.push(ConfigChildComponent.HistoryRoutine)
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
      navigation.replaceAll(ConfigChildComponent.Home)
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
    getNotesUseCase = getNotesUseCase,
    searchNoteUseCase = searchNoteUseCase,
    updateNoteUseCase = updateNoteUseCase,
    deleteNoteUseCase = deleteNoteUseCase,
    addNoteUseCase = addNoteUseCase,
  )

  private fun childComponent(
    config: ConfigChildComponent,
    childComponentContext: ComponentContext,
  ): RootComponent.ChildStack = when (config) {
    ConfigChildComponent.Home -> HomeStack(component = homeComponent(componentContext = childComponentContext))
    ConfigChildComponent.OnBoarding -> OnBoarding(component = onBoardingComponent(componentContext = childComponentContext))
    ConfigChildComponent.HistoryRoutine -> HistoryRoutine(component = historyRoutineComponent(componentContext = childComponentContext))
    ConfigChildComponent.Note -> Note(component = noteComponent(componentContext = childComponentContext))
    ConfigChildComponent.Routine -> Routine(component = routineComponent(componentContext = childComponentContext))
    else -> Routine(component = routineComponent(componentContext = childComponentContext))
  }
}
