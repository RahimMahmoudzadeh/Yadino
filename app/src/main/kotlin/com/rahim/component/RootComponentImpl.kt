package com.rahim.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.rahim.component.RootComponent.ChildStack.*
import com.rahim.component.config.AddNoteDialog
import com.rahim.yadino.core.timeDate.repo.DateTimeRepository
import com.rahim.yadino.home.domain.useCase.AddReminderUseCase
import com.rahim.yadino.home.domain.useCase.CancelReminderUseCase
import com.rahim.yadino.home.domain.useCase.DeleteReminderUseCase
import com.rahim.yadino.home.domain.useCase.GetCurrentDateUseCase
import com.rahim.yadino.home.domain.useCase.GetTodayRoutinesUseCase
import com.rahim.yadino.home.domain.useCase.SearchRoutineUseCase
import com.rahim.yadino.home.domain.useCase.UpdateReminderUseCase
import com.rahim.yadino.home.presentation.component.HomeComponent
import com.rahim.yadino.home.presentation.component.HomeComponentImpl
import com.rahim.yadino.home.presentation.component.addRoutineDialog.AddRoutineDialogComponent
import com.rahim.yadino.home.presentation.component.addRoutineDialog.AddRoutineDialogComponentImpl
import com.rahim.component.config.AddRoutineDialogHomeScreen
import com.rahim.component.config.AddRoutineDialogRoutineScreen
import com.rahim.component.config.ConfigChildComponent
import com.rahim.yadino.note.domain.useCase.AddNoteUseCase
import com.rahim.yadino.note.domain.useCase.DeleteNoteUseCase
import com.rahim.yadino.note.domain.useCase.GetNotesUseCase
import com.rahim.yadino.note.domain.useCase.SearchNoteUseCase
import com.rahim.yadino.note.domain.useCase.UpdateNoteUseCase
import com.rahim.yadino.note.presentation.component.NoteComponent
import com.rahim.yadino.note.presentation.component.NoteComponentImpl
import com.rahim.yadino.note.presentation.component.addNoteDialog.AddNoteDialogComponent
import com.rahim.yadino.note.presentation.component.addNoteDialog.AddNoteDialogComponentImpl
import com.rahim.yadino.onboarding.presentation.component.OnBoardingComponent
import com.rahim.yadino.onboarding.presentation.component.OnBoardingComponentImpl
import com.rahim.yadino.sharedPreferences.repo.SharedPreferencesRepository
import com.rahim.yadino.routine.domain.useCase.GetAllRoutineUseCase
import com.rahim.yadino.routine.domain.useCase.GetCurrentTimeUseCase
import com.rahim.yadino.routine.domain.useCase.GetRemindersUseCase
import com.rahim.yadino.routine.domain.useCase.GetTimesMonthUseCase
import com.yadino.routine.presentation.component.RoutineComponent
import com.yadino.routine.presentation.component.RoutineComponentImpl
import com.yadino.routine.presentation.component.history.HistoryRoutineComponent
import com.yadino.routine.presentation.component.history.HistoryRoutineComponentImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.builtins.serializer
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
  private val addRoutineDialogHomeScreenComponentNavigationSlot =
    SlotNavigation<AddRoutineDialogHomeScreen>()

  private val addRoutineDialogRoutineScreenComponentNavigationSlot =
    SlotNavigation<AddRoutineDialogRoutineScreen>()

  private val addNoteDialogComponentNavigationSlot =
    SlotNavigation<AddNoteDialog>()

  override val stack: Value<ChildStack<*, RootComponent.ChildStack>> = childStack(
    source = navigation,
    serializer = ConfigChildComponent.serializer(),
    initialConfiguration = ConfigChildComponent.Home,
    handleBackButton = true,
    childFactory = ::childComponent,
  )

  override val addRoutineDialogHomeScreen: Value<ChildSlot<AddRoutineDialogHomeScreen, AddRoutineDialogComponent>> =
    childSlot(
      source = addRoutineDialogHomeScreenComponentNavigationSlot,
      serializer = AddRoutineDialogHomeScreen.serializer(),
      handleBackButton = true,
      key = "addRoutineDialogHomeScreenComponentNavigationSlot",
    ) { config, childComponentContext ->
      AddRoutineDialogComponentImpl(
        componentContext = childComponentContext,
        mainDispatcher = Dispatchers.Main,
        addReminderUseCase = addReminderUseCase,
        updateReminderUseCase = updateReminderUseCase,
        routine = config.routine,
        onDismissed = addRoutineDialogHomeScreenComponentNavigationSlot::dismiss,
      )
    }

  private val getTimesMonthUseCase: GetTimesMonthUseCase = get()
  private val getCurrentTimeUseCase: GetCurrentTimeUseCase = get()

  override val addRoutineDialogRoutineScreen: Value<ChildSlot<AddRoutineDialogRoutineScreen, com.yadino.routine.presentation.component.addRoutineDialog.AddRoutineDialogComponent>> =
    childSlot(
      source = addRoutineDialogRoutineScreenComponentNavigationSlot,
      serializer = AddRoutineDialogRoutineScreen.serializer(),
      handleBackButton = true,
      key = "addRoutineDialogRoutineScreenComponentNavigationSlot",
    ) { config, childComponentContext ->
      com.yadino.routine.presentation.component.addRoutineDialog.AddRoutineDialogComponentImpl(
        componentContext = childComponentContext,
        mainDispatcher = Dispatchers.Main,
        ioDispatcher = Dispatchers.IO,
        updateRoutine = config.updateRoutine,
        addReminderUseCase = addReminderUseCaseRoutine,
        updateReminderUseCase = updateReminderUseCaseRoutine,
        getTimesMonthUseCase = getTimesMonthUseCase,
        getCurrentTimeUseCase = getCurrentTimeUseCase,
        onDismissed = addRoutineDialogRoutineScreenComponentNavigationSlot::dismiss,
      )
    }

  private val addNoteUseCase: AddNoteUseCase = get()
  private val updateNoteUseCase: UpdateNoteUseCase = get()

  override val addNoteDialog: Value<ChildSlot<AddNoteDialog, AddNoteDialogComponent>> =
    childSlot(
      source = addNoteDialogComponentNavigationSlot,
      serializer = AddNoteDialog.serializer(),
      handleBackButton = true,
      key = "addNoteDialogComponentNavigationSlot",
    ) { config, childComponentContext ->
      AddNoteDialogComponentImpl(
        componentContext = childComponentContext,
        mainDispatcher = Dispatchers.Main,
        ioDispatcher = Dispatchers.IO,
        addNoteUseCase = addNoteUseCase,
        updateNoteUseCase = updateNoteUseCase,
        updateNote = config.updateNote,
        onDismissed = addNoteDialogComponentNavigationSlot::dismiss,
      )
    }

  override fun onTabClick(tab: ConfigChildComponent) {
    navigation.bringToFront(tab)
  }

  override fun onShowAddDialogRoutineHomeScreen(dialog: AddRoutineDialogHomeScreen) {
    addRoutineDialogHomeScreenComponentNavigationSlot.activate(dialog)
  }

  override fun onShowAddDialogRoutineRoutineScreen(dialog: AddRoutineDialogRoutineScreen) {
    addRoutineDialogRoutineScreenComponentNavigationSlot.activate(dialog)
  }

  override fun onShowAddNoteDialog(dialog: AddNoteDialog) {
    addNoteDialogComponentNavigationSlot.activate(dialog)
  }

  private fun homeComponent(componentContext: ComponentContext): HomeComponent = HomeComponentImpl(
    componentContext = componentContext,
    mainContext = Dispatchers.Main,
    updateReminderUseCase = updateReminderUseCase,
    cancelReminderUseCase = cancelReminderUseCase,
    deleteReminderUseCase = deleteReminderUseCase,
    getTodayRoutinesUseCase = getTodayRoutinesUseCase,
    searchRoutineUseCase = searchRoutineUseCase,
    getCurrentDateUseCase = getCurrentDateUseCase,
    onShowUpdateRoutineDialog = { routineModel ->
      addRoutineDialogHomeScreenComponentNavigationSlot.activate(AddRoutineDialogHomeScreen(routineModel))
    },
  )

  private val sharedPreferencesRepository: SharedPreferencesRepository = get()
  private fun onBoardingComponent(componentContext: ComponentContext): OnBoardingComponent = OnBoardingComponentImpl(
    componentContext = componentContext,
    mainContext = Dispatchers.Main,
    sharedPreferencesRepository = sharedPreferencesRepository,
  )

  private val getAllRoutineUseCase: GetAllRoutineUseCase = get()
  private fun historyRoutineComponent(componentContext: ComponentContext): HistoryRoutineComponent = HistoryRoutineComponentImpl(
    componentContext = componentContext,
    mainContext = Dispatchers.Main,
    getAllRoutineUseCase = getAllRoutineUseCase,
  )

  private val addReminderUseCaseRoutine: com.rahim.yadino.routine.domain.useCase.AddReminderUseCase = get()
  private val updateReminderUseCaseRoutine: com.rahim.yadino.routine.domain.useCase.UpdateReminderUseCase = get()
  private val cancelReminderUseCaseRoutine: com.rahim.yadino.routine.domain.useCase.CancelReminderUseCase = get()
  private val deleteReminderUseCaseRoutine: com.rahim.yadino.routine.domain.useCase.DeleteReminderUseCase = get()
  private val getTodayRoutinesUseCaseRoutine: GetRemindersUseCase = get()
  private val searchRoutineUseCaseRoutine: com.rahim.yadino.routine.domain.useCase.SearchRoutineUseCase = get()
  private val dateTimeRepository: DateTimeRepository = get()

  private fun routineComponent(componentContext: ComponentContext): RoutineComponent = RoutineComponentImpl(
    componentContext = componentContext,
    mainContext = Dispatchers.Main,
    ioContext = Dispatchers.IO,
    cancelReminderUseCase = cancelReminderUseCaseRoutine,
    deleteReminderUseCase = deleteReminderUseCaseRoutine,
    getRemindersUseCase = getTodayRoutinesUseCaseRoutine,
    searchRoutineUseCase = searchRoutineUseCaseRoutine,
    dateTimeRepository = dateTimeRepository,
    onShowUpdateDialog = {
      addRoutineDialogRoutineScreenComponentNavigationSlot.activate(AddRoutineDialogRoutineScreen(it))
    },
  )

  private val deleteNoteUseCase: DeleteNoteUseCase = get()
  private val getNotesUseCase: GetNotesUseCase = get()
  private val searchNoteUseCase: SearchNoteUseCase = get()

  private fun noteComponent(componentContext: ComponentContext): NoteComponent = NoteComponentImpl(
    componentContext = componentContext,
    mainContext = Dispatchers.Main,
    deleteNoteUseCase = deleteNoteUseCase,
    getNotesUseCase = getNotesUseCase,
    searchNoteUseCase = searchNoteUseCase,
    updateNoteUseCase = updateNoteUseCase,
    onOpenUpdateNoteDialog = { updateNote ->
      addNoteDialogComponentNavigationSlot.activate(AddNoteDialog(updateNote))
    },
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
