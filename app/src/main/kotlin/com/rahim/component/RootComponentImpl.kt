package com.rahim.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import com.rahim.component.RootComponent.ChildStack.*
import com.rahim.component.config.AddNoteDialog
import com.rahim.component.config.AddRoutineDialogRoutineScreen
import com.rahim.component.config.ConfigChildComponent
import com.rahim.component.config.ErrorDialogNote
import com.rahim.component.config.ErrorDialogRoutine
import com.rahim.component.config.UpdateNoteDialog
import com.rahim.component.config.UpdateRoutineDialogRoutineScreen
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
import com.rahim.yadino.note.presentation.ui.root.component.NoteRootComponent
import com.rahim.yadino.note.presentation.ui.root.component.NoteRootComponentImpl
import com.rahim.yadino.note.presentation.ui.addNoteDialog.component.AddNoteDialogComponent
import com.rahim.yadino.note.presentation.ui.addNoteDialog.component.AddNoteDialogComponentImpl
import com.rahim.yadino.note.presentation.ui.updateNoteDialog.component.UpdateNoteDialogComponent
import com.rahim.yadino.note.presentation.ui.updateNoteDialog.component.UpdateNoteDialogComponentImpl
import com.rahim.yadino.note.presentation.ui.errorDialog.component.ErrorDialogComponent
import com.rahim.yadino.note.presentation.ui.errorDialog.component.ErrorDialogComponentImpl
import com.rahim.yadino.onboarding.presentation.component.OnBoardingComponent
import com.rahim.yadino.onboarding.presentation.component.OnBoardingComponentImpl
import com.rahim.yadino.routine.domain.useCase.GetAllRoutineUseCase
import com.rahim.yadino.routine.domain.useCase.GetCurrentTimeUseCase
import com.rahim.yadino.routine.domain.useCase.GetRemindersUseCase
import com.rahim.yadino.routine.domain.useCase.GetTimesMonthUseCase
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

  private val addRoutineDialogRoutineScreenComponentNavigationSlot =
    SlotNavigation<AddRoutineDialogRoutineScreen>()

  private val updateRoutineDialogRoutineScreenComponentNavigationSlot =
    SlotNavigation<UpdateRoutineDialogRoutineScreen>()

  private val addNoteDialogComponentNavigationSlot =
    SlotNavigation<AddNoteDialog>()

  private val updateNoteDialogComponentNavigationSlot =
    SlotNavigation<UpdateNoteDialog>()

  private val errorDialogNoteComponentNavigationSlot =
    SlotNavigation<ErrorDialogNote>()

  private val errorDialogRoutineComponentNavigationSlot =
    SlotNavigation<ErrorDialogRoutine>()

  override val stack: Value<ChildStack<*, RootComponent.ChildStack>> = childStack(
    source = navigation,
    serializer = ConfigChildComponent.serializer(),
    initialConfiguration = ConfigChildComponent.Home,
    handleBackButton = true,
    childFactory = ::childComponent,
  )


  private val getTimesMonthUseCase: GetTimesMonthUseCase = get()
  private val getCurrentTimeUseCase: GetCurrentTimeUseCase = get()

  override val addRoutineDialogRoutineScreen: Value<ChildSlot<AddRoutineDialogRoutineScreen, com.rahim.yadino.routine.presentation.component.addRoutineDialog.AddRoutineDialogComponent>> =
    childSlot(
      source = addRoutineDialogRoutineScreenComponentNavigationSlot,
      serializer = AddRoutineDialogRoutineScreen.serializer(),
      handleBackButton = true,
      key = "addRoutineDialogRoutineScreenComponentNavigationSlot",
    ) { config, childComponentContext ->
      com.rahim.yadino.routine.presentation.component.addRoutineDialog.AddRoutineDialogComponentImpl(
        componentContext = childComponentContext,
        mainDispatcher = Dispatchers.Main,
        ioDispatcher = Dispatchers.IO,
        addReminderUseCase = addReminderUseCaseRoutine,
        getTimesMonthUseCase = getTimesMonthUseCase,
        getCurrentTimeUseCase = getCurrentTimeUseCase,
        onDismissed = addRoutineDialogRoutineScreenComponentNavigationSlot::dismiss,
      )
    }
  private val updateReminderUseCaseRoutine: com.rahim.yadino.routine.domain.useCase.UpdateReminderUseCase = get()

  override val updateRoutineDialogRoutineScreen: Value<ChildSlot<UpdateRoutineDialogRoutineScreen, com.rahim.yadino.routine.presentation.component.updateRoutineDialog.UpdateRoutineDialogComponent>> =
    childSlot(
      source = updateRoutineDialogRoutineScreenComponentNavigationSlot,
      serializer = UpdateRoutineDialogRoutineScreen.serializer(),
      handleBackButton = true,
      key = "updateRoutineDialogRoutineScreenComponentNavigationSlot",
    ) { config, childComponentContext ->
      com.rahim.yadino.routine.presentation.component.updateRoutineDialog.UpdateRoutineDialogComponentImpl(
        componentContext = childComponentContext,
        mainDispatcher = Dispatchers.Main,
        ioDispatcher = Dispatchers.IO,
        updateReminderUseCase = updateReminderUseCaseRoutine,
        updateRoutine = config.updateRoutine,
        getTimesMonthUseCase = getTimesMonthUseCase,
        getCurrentTimeUseCase = getCurrentTimeUseCase,
        onDismissed = updateRoutineDialogRoutineScreenComponentNavigationSlot::dismiss,
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
        onDismissed = addNoteDialogComponentNavigationSlot::dismiss,
      )
    }
  override val updateNoteDialog: Value<ChildSlot<UpdateNoteDialog, UpdateNoteDialogComponent>> =
    childSlot(
      source = updateNoteDialogComponentNavigationSlot,
      serializer = UpdateNoteDialog.serializer(),
      handleBackButton = true,
      key = "updateNoteDialogComponentNavigationSlot",
    ) { config, childComponentContext ->
      UpdateNoteDialogComponentImpl(
        componentContext = childComponentContext,
        mainDispatcher = Dispatchers.Main,
        ioDispatcher = Dispatchers.IO,
        updateNoteUseCase = updateNoteUseCase,
        updateNote = config.updateNote,
        onDismissed = updateNoteDialogComponentNavigationSlot::dismiss,
      )
    }


  private val deleteReminderUseCaseRoutine: com.rahim.yadino.routine.domain.useCase.DeleteReminderUseCase = get()

  override val errorDialogRoutineScreen: Value<ChildSlot<ErrorDialogRoutine, com.rahim.yadino.routine.presentation.component.errorDialog.ErrorDialogComponent>> =
    childSlot(
      source = errorDialogRoutineComponentNavigationSlot,
      serializer = ErrorDialogRoutine.serializer(),
      handleBackButton = true,
      key = "errorDialogRoutineComponentNavigationSlot",
    ) { config, childComponentContext ->
      com.rahim.yadino.routine.presentation.component.errorDialog.ErrorDialogComponentImpl(
        componentContext = childComponentContext,
        mainContext = Dispatchers.Main,
        deleteReminderUseCase = deleteReminderUseCaseRoutine,
        errorDialogUiModel = config.errorDialogUiModel,
        onDismissed = errorDialogRoutineComponentNavigationSlot::dismiss,
      )
    }

  override val errorDialogNoteScreen: Value<ChildSlot<ErrorDialogNote, ErrorDialogComponent>> =
    childSlot(
      source = errorDialogNoteComponentNavigationSlot,
      serializer = ErrorDialogNote.serializer(),
      handleBackButton = true,
      key = "errorDialogNoteComponentNavigationSlot",
    ) { config, childComponentContext ->
      ErrorDialogComponentImpl(
        componentContext = childComponentContext,
        mainContext = Dispatchers.Main,
        deleteNoteUseCase = deleteNoteUseCase,
        errorDialogUiModel = config.errorDialogUiModel,
        onDismissed = errorDialogNoteComponentNavigationSlot::dismiss,
      )
    }

  override fun onTabClick(tab: ConfigChildComponent) {
    navigation.bringToFront(tab)
  }


  override fun onShowAddDialogRoutineRoutineScreen(dialog: AddRoutineDialogRoutineScreen) {
    addRoutineDialogRoutineScreenComponentNavigationSlot.activate(dialog)
  }

  override fun onShowAddNoteDialog(dialog: AddNoteDialog) {
    addNoteDialogComponentNavigationSlot.activate(dialog)
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

  private val sharedPreferencesRepository: SharedPreferencesRepository = get()
  private fun onBoardingComponent(componentContext: ComponentContext): OnBoardingComponent = OnBoardingComponentImpl(
    componentContext = componentContext,
    mainContext = Dispatchers.Main,
    sharedPreferencesRepository = sharedPreferencesRepository,
    onNavigateToHome = {
      navigation.replaceAll(ConfigChildComponent.Home)
    },
  )

  private val getAllRoutineUseCase: GetAllRoutineUseCase = get()
  private fun historyRoutineComponent(componentContext: ComponentContext): com.rahim.yadino.routine.presentation.component.history.HistoryRoutineComponent = _root_ide_package_.com.rahim.yadino.routine.presentation.component.history.HistoryRoutineComponentImpl(
    componentContext = componentContext,
    mainContext = Dispatchers.Main,
    getAllRoutineUseCase = getAllRoutineUseCase,
  )

  private val addReminderUseCaseRoutine: com.rahim.yadino.routine.domain.useCase.AddReminderUseCase = get()
  private val cancelReminderUseCaseRoutine: com.rahim.yadino.routine.domain.useCase.CancelReminderUseCase = get()
  private val getTodayRoutinesUseCaseRoutine: GetRemindersUseCase = get()
  private val searchRoutineUseCaseRoutine: com.rahim.yadino.routine.domain.useCase.SearchRoutineUseCase = get()
  private val dateTimeRepository: DateTimeRepository = get()

  private fun routineComponent(componentContext: ComponentContext): com.rahim.yadino.routine.presentation.component.RoutineComponent = _root_ide_package_.com.rahim.yadino.routine.presentation.component.RoutineComponentImpl(
    componentContext = componentContext,
    mainContext = Dispatchers.Main,
    ioContext = Dispatchers.IO,
    cancelReminderUseCase = cancelReminderUseCaseRoutine,
    getRemindersUseCase = getTodayRoutinesUseCaseRoutine,
    searchRoutineUseCase = searchRoutineUseCaseRoutine,
    dateTimeRepository = dateTimeRepository,
    onShowUpdateDialog = {
      updateRoutineDialogRoutineScreenComponentNavigationSlot.activate(UpdateRoutineDialogRoutineScreen(it))
    },
    onShowErrorDialog = {
      errorDialogRoutineComponentNavigationSlot.activate(ErrorDialogRoutine(it))
    },
  )

  private val deleteNoteUseCase: DeleteNoteUseCase = get()
  private val getNotesUseCase: GetNotesUseCase = get()
  private val searchNoteUseCase: SearchNoteUseCase = get()

  private fun noteComponent(componentContext: ComponentContext): NoteRootComponent = NoteRootComponentImpl(
    componentContext = componentContext,
    mainContext = Dispatchers.Main,
    getNotesUseCase = getNotesUseCase,
    searchNoteUseCase = searchNoteUseCase,
    updateNoteUseCase = updateNoteUseCase,
    onOpenUpdateNoteDialog = { updateNote ->
      updateNoteDialogComponentNavigationSlot.activate(UpdateNoteDialog(updateNote))
    },
    onShowErrorDialog = { errorDialogUiModel ->
      errorDialogNoteComponentNavigationSlot.activate(ErrorDialogNote(errorDialogUiModel))
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
