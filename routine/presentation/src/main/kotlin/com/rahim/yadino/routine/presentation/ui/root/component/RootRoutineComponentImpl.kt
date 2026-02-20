package com.rahim.yadino.routine.presentation.ui.root.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.rahim.yadino.core.timeDate.repo.DateTimeRepository
import com.rahim.yadino.routine.domain.useCase.AddReminderUseCase
import com.rahim.yadino.routine.domain.useCase.CancelReminderUseCase
import com.rahim.yadino.routine.domain.useCase.DeleteReminderUseCase
import com.rahim.yadino.routine.domain.useCase.GetCurrentTimeUseCase
import com.rahim.yadino.routine.domain.useCase.GetRemindersUseCase
import com.rahim.yadino.routine.domain.useCase.GetTimesMonthUseCase
import com.rahim.yadino.routine.domain.useCase.SearchRoutineUseCase
import com.rahim.yadino.routine.domain.useCase.UpdateReminderUseCase
import com.rahim.yadino.routine.presentation.model.ErrorDialogRemoveRoutineUiModel
import com.rahim.yadino.routine.presentation.model.ErrorDialogUiModel
import com.rahim.yadino.routine.presentation.model.RoutineUiModel
import com.rahim.yadino.routine.presentation.ui.addRoutineDialog.component.AddRoutineDialogComponent
import com.rahim.yadino.routine.presentation.ui.addRoutineDialog.component.AddRoutineDialogComponentImpl
import com.rahim.yadino.routine.presentation.ui.errorDialog.component.ErrorDialogComponent
import com.rahim.yadino.routine.presentation.ui.errorDialog.component.ErrorDialogComponentImpl
import com.rahim.yadino.routine.presentation.ui.errorDialogRemoveRoutine.component.ErrorDialogRemoveRoutineComponent
import com.rahim.yadino.routine.presentation.ui.errorDialogRemoveRoutine.component.ErrorDialogRemoveRoutineComponentImpl
import com.rahim.yadino.routine.presentation.ui.main.component.MainRoutineComponent
import com.rahim.yadino.routine.presentation.ui.main.component.MainRoutineComponentImpl
import com.rahim.yadino.routine.presentation.ui.updateDialogRoutine.component.UpdateRoutineDialogComponent
import com.rahim.yadino.routine.presentation.ui.updateDialogRoutine.component.UpdateRoutineDialogComponentImpl
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class RootRoutineComponentImpl(
  componentContext: ComponentContext,
  private val mainContext: CoroutineContext,
  private val ioContext: CoroutineContext,
  private val addReminderUseCase: AddReminderUseCase,
  private val getTimesMonthUseCase: GetTimesMonthUseCase,
  private val getCurrentTimeUseCase: GetCurrentTimeUseCase,
  private val updateReminderUseCase: UpdateReminderUseCase,
  private val deleteReminderUseCase: DeleteReminderUseCase,
  private val cancelReminderUseCase: CancelReminderUseCase,
  private val getRemindersUseCase: GetRemindersUseCase,
  private val searchRoutineUseCase: SearchRoutineUseCase,
  private val dateTimeRepository: DateTimeRepository,
) : RootRoutineComponent, ComponentContext by componentContext {

  private val addRoutineDialogNavigationSlot =
    SlotNavigation<RootRoutineComponent.DialogSlot.AddRoutineDialog>()

  private val updateRoutineDialogNavigationSlot =
    SlotNavigation<RootRoutineComponent.DialogSlot.UpdateRoutineDialog>()

  private val errorDialogRemoveRoutineNavigationSlot =
    SlotNavigation<RootRoutineComponent.DialogSlot.ErrorDialogRemoveRoutine>()

  private val errorDialogNavigationSlot =
    SlotNavigation<RootRoutineComponent.DialogSlot.ErrorDialog>()

  override val addRoutineDialogScreen: Value<ChildSlot<RootRoutineComponent.DialogSlot.AddRoutineDialog, AddRoutineDialogComponent>> =
    childSlot(
      source = addRoutineDialogNavigationSlot,
      serializer = RootRoutineComponent.DialogSlot.AddRoutineDialog.serializer(),
      handleBackButton = true,
      key = "addRoutineDialogNavigationSlot",
    ) { config, childComponentContext ->
      AddRoutineDialogComponentImpl(
        componentContext = childComponentContext,
        mainDispatcher = Dispatchers.Main,
        ioDispatcher = Dispatchers.IO,
        addReminderUseCase = addReminderUseCase,
        getTimesMonthUseCase = getTimesMonthUseCase,
        getCurrentTimeUseCase = getCurrentTimeUseCase,
        onDismissed = addRoutineDialogNavigationSlot::dismiss,
      )
    }

  override val updateRoutineDialogScreen: Value<ChildSlot<RootRoutineComponent.DialogSlot.UpdateRoutineDialog, UpdateRoutineDialogComponent>> =
    childSlot(
      source = updateRoutineDialogNavigationSlot,
      serializer = RootRoutineComponent.DialogSlot.UpdateRoutineDialog.serializer(),
      handleBackButton = true,
      key = "updateRoutineDialogScreen",
    ) { config, childComponentContext ->
      UpdateRoutineDialogComponentImpl(
        componentContext = childComponentContext,
        mainDispatcher = Dispatchers.Main,
        ioDispatcher = Dispatchers.IO,
        updateReminderUseCase = updateReminderUseCase,
        updateRoutine = config.updateRoutine,
        getTimesMonthUseCase = getTimesMonthUseCase,
        getCurrentTimeUseCase = getCurrentTimeUseCase,
        onDismissed = updateRoutineDialogNavigationSlot::dismiss,
      )
    }

  override val errorDialogRemoveRoutineScreen: Value<ChildSlot<RootRoutineComponent.DialogSlot.ErrorDialogRemoveRoutine, ErrorDialogRemoveRoutineComponent>> =
    childSlot(
      source = errorDialogRemoveRoutineNavigationSlot,
      serializer = RootRoutineComponent.DialogSlot.ErrorDialogRemoveRoutine.serializer(),
      handleBackButton = true,
      key = "errorDialogRemoveRoutineNavigationSlot",
    ) { config, childComponentContext ->
      ErrorDialogRemoveRoutineComponentImpl(
        componentContext = childComponentContext,
        mainContext = Dispatchers.Main,
        deleteReminderUseCase = deleteReminderUseCase,
        errorDialogRemoveRoutineUiModel = config.errorDialogRemoveRoutineUiModel,
        onDismissed = errorDialogRemoveRoutineNavigationSlot::dismiss,
      )
    }

  override val errorDialogScreen: Value<ChildSlot<RootRoutineComponent.DialogSlot.ErrorDialog, ErrorDialogComponent>> =
    childSlot(
      source = errorDialogNavigationSlot,
      serializer = RootRoutineComponent.DialogSlot.ErrorDialog.serializer(),
      handleBackButton = true,
      key = "errorDialogNavigationSlot",
    ) { config, childComponentContext ->
      ErrorDialogComponentImpl(
        componentContext = childComponentContext,
        mainContext = Dispatchers.Main,
        errorDialogUiModel = config.errorDialogUiModel,
        onDismissed = errorDialogNavigationSlot::dismiss,
      )
    }

  private val navigation = StackNavigation<RootRoutineComponent.ChildConfig>()

  override val stack: Value<ChildStack<*, RootRoutineComponent.ChildStack>> = childStack(
    source = navigation,
    serializer = RootRoutineComponent.ChildConfig.serializer(),
    initialConfiguration = RootRoutineComponent.ChildConfig.RoutineMain,
    handleBackButton = true,
    childFactory = ::childComponent,
  )
  private fun childComponent(
    config: RootRoutineComponent.ChildConfig,
    childComponentContext: ComponentContext,
  ): RootRoutineComponent.ChildStack = when (config) {
    RootRoutineComponent.ChildConfig.RoutineMain -> RootRoutineComponent.ChildStack.RoutineMainStack(component = mainComponent(componentContext = childComponentContext))
  }

  private fun mainComponent(componentContext: ComponentContext): MainRoutineComponent = MainRoutineComponentImpl(
    componentContext = componentContext,
    mainContext = mainContext,
    ioContext = ioContext,
    cancelReminderUseCase = cancelReminderUseCase,
    getRemindersUseCase = getRemindersUseCase,
    searchRoutineUseCase = searchRoutineUseCase,
    dateTimeRepository = dateTimeRepository,
    showErrorDialogRemoveRoutine = ::showErrorDialogRemoveRoutine,
    showUpdateDialog = ::showUpdateDialog,
    showErrorDialog = ::showErrorDialog,
    showAddDialog = ::showAddDialog,
  )

  private fun showErrorDialogRemoveRoutine(errorDialogRemoveRoutineUiModel: ErrorDialogRemoveRoutineUiModel) {
    errorDialogRemoveRoutineNavigationSlot.activate(RootRoutineComponent.DialogSlot.ErrorDialogRemoveRoutine(errorDialogRemoveRoutineUiModel))
  }

  private fun showUpdateDialog(routine: RoutineUiModel) {
    updateRoutineDialogNavigationSlot.activate(RootRoutineComponent.DialogSlot.UpdateRoutineDialog(routine))
  }

  private fun showAddDialog() {
    addRoutineDialogNavigationSlot.activate(RootRoutineComponent.DialogSlot.AddRoutineDialog)
  }

  private fun showErrorDialog(errorDialogUiModel: ErrorDialogUiModel) {
    errorDialogNavigationSlot.activate(RootRoutineComponent.DialogSlot.ErrorDialog(errorDialogUiModel))
  }
}
