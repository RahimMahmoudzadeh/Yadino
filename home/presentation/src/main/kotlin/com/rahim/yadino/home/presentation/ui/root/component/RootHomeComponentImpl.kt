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
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.rahim.yadino.home.domain.useCase.UpdateReminderUseCase
import com.rahim.yadino.home.domain.useCase.AddReminderUseCase
import com.rahim.yadino.home.domain.useCase.CancelReminderUseCase
import com.rahim.yadino.home.domain.useCase.DeleteReminderUseCase
import com.rahim.yadino.home.domain.useCase.GetCurrentDateUseCase
import com.rahim.yadino.home.domain.useCase.GetTodayRoutinesUseCase
import com.rahim.yadino.home.domain.useCase.SearchRoutineUseCase
import com.rahim.yadino.home.presentation.model.ErrorDialogRemoveUiModel
import com.rahim.yadino.home.presentation.model.ErrorDialogUiModel
import com.rahim.yadino.home.presentation.model.RoutineUiModel
import com.rahim.yadino.home.presentation.ui.addDialogRoutine.component.AddRoutineDialogComponent
import com.rahim.yadino.home.presentation.ui.addDialogRoutine.component.AddRoutineDialogComponentImpl
import com.rahim.yadino.home.presentation.ui.errorDialog.component.ErrorDialogComponent
import com.rahim.yadino.home.presentation.ui.errorDialog.component.ErrorDialogComponentImpl
import com.rahim.yadino.home.presentation.ui.errorDialogRemoveRoutine.component.ErrorDialogRemoveRoutineComponent
import com.rahim.yadino.home.presentation.ui.errorDialogRemoveRoutine.component.ErrorDialogRemoveRoutineComponentImpl
import com.rahim.yadino.home.presentation.ui.main.component.MainComponentImpl
import com.rahim.yadino.home.presentation.ui.main.component.MainHomeComponent
import com.rahim.yadino.home.presentation.ui.root.component.DialogSlotHomeComponent.*
import com.rahim.yadino.home.presentation.ui.root.component.config.ChildConfigHomeComponent
import com.rahim.yadino.home.presentation.ui.updateDialogRoutine.component.UpdateRoutineDialogComponent
import com.rahim.yadino.home.presentation.ui.updateDialogRoutine.component.UpdateRoutineDialogComponentImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlin.coroutines.CoroutineContext

class RootHomeComponentImpl(
  componentContext: ComponentContext,
  mainContext: CoroutineContext,
  private val updateReminderUseCase: UpdateReminderUseCase,
  private val cancelReminderUseCase: CancelReminderUseCase,
  private val getTodayRoutinesUseCase: GetTodayRoutinesUseCase,
  private val searchRoutineUseCase: SearchRoutineUseCase,
  private val getCurrentDateUseCase: GetCurrentDateUseCase,
  private val deleteReminderUseCase: DeleteReminderUseCase,
  private val addReminderUseCase: AddReminderUseCase,
) : RootHomeComponent, ComponentContext by componentContext {

  private val addRoutineDialogNavigationSlot =
    SlotNavigation<AddRoutineDialog>()

  private val updateRoutineDialogNavigationSlot =
    SlotNavigation<UpdateRoutineDialog>()

  private val errorDialogRemoveRoutineNavigationSlot =
    SlotNavigation<ErrorDialogRemoveRoutine>()

  private val errorDialogNavigationSlot =
    SlotNavigation<ErrorDialog>()


  override val addRoutineDialogScreen: Value<ChildSlot<AddRoutineDialog, AddRoutineDialogComponent>> =
    childSlot(
      source = addRoutineDialogNavigationSlot,
      serializer = AddRoutineDialog.serializer(),
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

  override val updateRoutineDialogScreen: Value<ChildSlot<DialogSlotHomeComponent.UpdateRoutineDialog, UpdateRoutineDialogComponent>> =
    childSlot(
      source = updateRoutineDialogNavigationSlot,
      serializer = UpdateRoutineDialog.serializer(),
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

  override val errorDialogRemoveRoutineScreen: Value<ChildSlot<ErrorDialogRemoveRoutine, ErrorDialogRemoveRoutineComponent>> =
    childSlot(
      source = errorDialogRemoveRoutineNavigationSlot,
      serializer = ErrorDialogRemoveRoutine.serializer(),
      handleBackButton = true,
      key = "errorDialogRemoveRoutineComponentNavigationSlot",
    ) { config, childComponentContext ->
      ErrorDialogRemoveRoutineComponentImpl(
        componentContext = childComponentContext,
        mainContext = Dispatchers.Main,
        deleteReminderUseCase = deleteReminderUseCase,
        errorDialogRemoveUiModel = config.errorDialogRemoveUiModel,
        onDismissed = errorDialogRemoveRoutineNavigationSlot::dismiss,
      )
    }

  override val errorDialogScreen: Value<ChildSlot<ErrorDialog, ErrorDialogComponent>> =
    childSlot(
      source = errorDialogNavigationSlot,
      serializer = DialogSlotHomeComponent.ErrorDialog.serializer(),
      handleBackButton = true,
      key = "errorDialogComponentNavigationSlot",
    ) { config, childComponentContext ->
      ErrorDialogComponentImpl(
        componentContext = childComponentContext,
        mainContext = Dispatchers.Main,
        errorDialogUiModel = config.errorDialogUiModel,
        onDismissed = errorDialogNavigationSlot::dismiss,
      )
    }

  private val navigation = StackNavigation<ChildConfigHomeComponent>()

  override val stack: Value<ChildStack<*, RootHomeComponent.ChildStack>> = childStack(
    source = navigation,
    serializer = ChildConfigHomeComponent.serializer(),
    initialConfiguration = ChildConfigHomeComponent.HomeMain,
    handleBackButton = true,
    childFactory = ::childComponent,
  )
  private fun childComponent(
    config: ChildConfigHomeComponent,
    childComponentContext: ComponentContext,
  ): RootHomeComponent.ChildStack = when (config) {
    ChildConfigHomeComponent.HomeMain -> RootHomeComponent.ChildStack.HomeMainStack(component = homeComponent(componentContext = childComponentContext))
  }

  private fun homeComponent(componentContext: ComponentContext): MainHomeComponent = MainComponentImpl(
    componentContext = componentContext,
    mainContext = Dispatchers.Main,
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
    errorDialogRemoveRoutineNavigationSlot.activate(ErrorDialogRemoveRoutine(errorDialogRemoveUiModel))
  }

  private fun showErrorDialog(errorDialogUiModel: ErrorDialogUiModel) {
    errorDialogNavigationSlot.activate(ErrorDialog(errorDialogUiModel))
  }

  private fun showAddDialogRoutine() {
    addRoutineDialogNavigationSlot.activate(AddRoutineDialog)
  }

  private fun showUpdateDialogRoutine(updateRoutine: RoutineUiModel) {
    updateRoutineDialogNavigationSlot.activate(UpdateRoutineDialog(updateRoutine))
  }

}
