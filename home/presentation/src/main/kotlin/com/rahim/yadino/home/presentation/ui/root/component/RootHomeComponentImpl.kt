package com.rahim.yadino.home.presentation.ui.root.component

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
import com.rahim.yadino.home.domain.useCase.AddReminderUseCase
import com.rahim.yadino.home.domain.useCase.CancelReminderUseCase
import com.rahim.yadino.home.domain.useCase.DeleteReminderUseCase
import com.rahim.yadino.home.domain.useCase.GetCurrentDateUseCase
import com.rahim.yadino.home.domain.useCase.GetTodayRoutinesUseCase
import com.rahim.yadino.home.domain.useCase.SearchRoutineUseCase
import com.rahim.yadino.home.domain.useCase.UpdateReminderUseCase
import com.rahim.yadino.home.presentation.model.ErrorDialogRemoveUiModel
import com.rahim.yadino.home.presentation.model.ErrorDialogUiModel
import com.rahim.yadino.home.presentation.model.RoutineUiModel
import com.rahim.yadino.home.presentation.ui.addDialogRoutine.component.AddRoutineDialogComponent
import com.rahim.yadino.home.presentation.ui.addDialogRoutine.component.AddRoutineDialogComponentImpl
import com.rahim.yadino.home.presentation.ui.errorDialog.component.ErrorDialogComponent
import com.rahim.yadino.home.presentation.ui.errorDialog.component.ErrorDialogComponentImpl
import com.rahim.yadino.home.presentation.ui.errorDialogRemoveRoutine.component.ErrorDialogRemoveRoutineComponent
import com.rahim.yadino.home.presentation.ui.errorDialogRemoveRoutine.component.ErrorDialogRemoveRoutineComponentImpl
import com.rahim.yadino.home.presentation.ui.main.component.MainHomeComponent
import com.rahim.yadino.home.presentation.ui.main.component.MainHomeComponentImpl
import com.rahim.yadino.home.presentation.ui.updateDialogRoutine.component.UpdateRoutineDialogComponent
import com.rahim.yadino.home.presentation.ui.updateDialogRoutine.component.UpdateRoutineDialogComponentImpl
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class RootHomeComponentImpl(
  componentContext: ComponentContext,
  private val mainContext: CoroutineContext,
  private val updateReminderUseCase: UpdateReminderUseCase,
  private val cancelReminderUseCase: CancelReminderUseCase,
  private val getTodayRoutinesUseCase: GetTodayRoutinesUseCase,
  private val searchRoutineUseCase: SearchRoutineUseCase,
  private val getCurrentDateUseCase: GetCurrentDateUseCase,
  private val deleteReminderUseCase: DeleteReminderUseCase,
  private val addReminderUseCase: AddReminderUseCase,
) : RootHomeComponent, ComponentContext by componentContext {

  private val addRoutineDialogNavigationSlot =
    SlotNavigation<RootHomeComponent.DialogSlot.AddRoutineDialog>()

  private val updateRoutineDialogNavigationSlot =
    SlotNavigation<RootHomeComponent.DialogSlot.UpdateRoutineDialog>()

  private val errorDialogRemoveRoutineNavigationSlot =
    SlotNavigation<RootHomeComponent.DialogSlot.ErrorDialogRemoveRoutine>()

  private val errorDialogNavigationSlot =
    SlotNavigation<RootHomeComponent.DialogSlot.ErrorDialog>()


  override val addRoutineDialogScreen: Value<ChildSlot<RootHomeComponent.DialogSlot.AddRoutineDialog, AddRoutineDialogComponent>> =
    childSlot(
      source = addRoutineDialogNavigationSlot,
      serializer = RootHomeComponent.DialogSlot.AddRoutineDialog.serializer(),
      handleBackButton = true,
      key = "addRoutineDialogNavigationSlot",
    ) { config, childComponentContext ->
      AddRoutineDialogComponentImpl(
        componentContext = childComponentContext,
        mainDispatcher = Dispatchers.Main,
        addReminderUseCase = addReminderUseCase,
        onDismissed = addRoutineDialogNavigationSlot::dismiss,
      )
    }

  override val updateRoutineDialogScreen: Value<ChildSlot<RootHomeComponent.DialogSlot.UpdateRoutineDialog, UpdateRoutineDialogComponent>> =
    childSlot(
      source = updateRoutineDialogNavigationSlot,
      serializer = RootHomeComponent.DialogSlot.UpdateRoutineDialog.serializer(),
      handleBackButton = true,
      key = "updateRoutineDialogNavigationSlot",
    ) { config, childComponentContext ->
      UpdateRoutineDialogComponentImpl(
        componentContext = childComponentContext,
        mainDispatcher = Dispatchers.Main,
        updateReminderUseCase = updateReminderUseCase,
        updateRoutine = config.updateRoutine,
        onDismissed = updateRoutineDialogNavigationSlot::dismiss,
      )
    }

  override val errorDialogRemoveRoutineScreen: Value<ChildSlot<RootHomeComponent.DialogSlot.ErrorDialogRemoveRoutine, ErrorDialogRemoveRoutineComponent>> =
    childSlot(
      source = errorDialogRemoveRoutineNavigationSlot,
      serializer = RootHomeComponent.DialogSlot.ErrorDialogRemoveRoutine.serializer(),
      handleBackButton = true,
      key = "errorDialogRemoveRoutineComponentNavigationSlot",
    ) { config, childComponentContext ->
      ErrorDialogRemoveRoutineComponentImpl(
        componentContext = childComponentContext,
        mainContext = mainContext,
        deleteReminderUseCase = deleteReminderUseCase,
        errorDialogRemoveUiModel = config.errorDialogRemoveUiModel,
        onDismissed = errorDialogRemoveRoutineNavigationSlot::dismiss,
      )
    }

  override val errorDialogScreen: Value<ChildSlot<RootHomeComponent.DialogSlot.ErrorDialog, ErrorDialogComponent>> =
    childSlot(
      source = errorDialogNavigationSlot,
      serializer = RootHomeComponent.DialogSlot.ErrorDialog.serializer(),
      handleBackButton = true,
      key = "errorDialogComponentNavigationSlot",
    ) { config, childComponentContext ->
      ErrorDialogComponentImpl(
        componentContext = childComponentContext,
        mainContext = mainContext,
        errorDialogUiModel = config.errorDialogUiModel,
        onDismissed = errorDialogNavigationSlot::dismiss,
      )
    }

  private val navigation = StackNavigation<RootHomeComponent.ChildConfig>()

  override val stack: Value<ChildStack<*, RootHomeComponent.ChildStack>> = childStack(
    source = navigation,
    serializer = RootHomeComponent.ChildConfig.serializer(),
    initialConfiguration = RootHomeComponent.ChildConfig.HomeMain,
    handleBackButton = true,
    childFactory = ::childComponent,
  )
  private fun childComponent(
    config: RootHomeComponent.ChildConfig,
    childComponentContext: ComponentContext,
  ): RootHomeComponent.ChildStack = when (config) {
    RootHomeComponent.ChildConfig.HomeMain -> RootHomeComponent.ChildStack.HomeMainStack(component = homeComponent(componentContext = childComponentContext))
  }

  private fun homeComponent(componentContext: ComponentContext): MainHomeComponent = MainHomeComponentImpl(
    componentContext = componentContext,
    mainContext = mainContext,
    cancelReminderUseCase = cancelReminderUseCase,
    getTodayRoutinesUseCase = getTodayRoutinesUseCase,
    searchRoutineUseCase = searchRoutineUseCase,
    getCurrentDateUseCase = getCurrentDateUseCase,
    updateReminderUseCase = updateReminderUseCase,
    showErrorRemoveRoutineDialog = ::showErrorDialog,
    showUpdateRoutineDialog = ::showUpdateDialogRoutine,
    showAddRoutineDialog = ::showAddDialogRoutine,
    showErrorDialog = ::showErrorDialog,
  )

  private fun showErrorDialog(errorDialogRemoveUiModel: ErrorDialogRemoveUiModel) {
    errorDialogRemoveRoutineNavigationSlot.activate(RootHomeComponent.DialogSlot.ErrorDialogRemoveRoutine(errorDialogRemoveUiModel))
  }

  private fun showErrorDialog(errorDialogUiModel: ErrorDialogUiModel) {
    errorDialogNavigationSlot.activate(RootHomeComponent.DialogSlot.ErrorDialog(errorDialogUiModel))
  }

  private fun showAddDialogRoutine() {
    addRoutineDialogNavigationSlot.activate(RootHomeComponent.DialogSlot.AddRoutineDialog)
  }

  private fun showUpdateDialogRoutine(updateRoutine: RoutineUiModel) {
    updateRoutineDialogNavigationSlot.activate(RootHomeComponent.DialogSlot.UpdateRoutineDialog(updateRoutine))
  }

}
