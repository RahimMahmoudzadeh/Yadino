package com.rahim.yadino.home.presentation.ui.root.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.rahim.yadino.home.domain.useCase.CancelReminderUseCase
import com.rahim.yadino.home.domain.useCase.GetCurrentDateUseCase
import com.rahim.yadino.home.domain.useCase.GetTodayRoutinesUseCase
import com.rahim.yadino.home.domain.useCase.SearchRoutineUseCase
import com.rahim.yadino.home.domain.useCase.UpdateReminderUseCase
import com.rahim.yadino.base.LoadableData
import com.rahim.yadino.base.Resource
import com.rahim.yadino.base.toMessageUi
import com.rahim.yadino.enums.message.error.ErrorMessage
import com.rahim.yadino.home.domain.useCase.AddReminderUseCase
import com.rahim.yadino.home.domain.useCase.DeleteReminderUseCase
import com.rahim.yadino.home.presentation.mapper.toCurrentDatePresentationLayer
import com.rahim.yadino.home.presentation.mapper.toRoutine
import com.rahim.yadino.home.presentation.mapper.toRoutineUiModel
import com.rahim.yadino.home.presentation.model.ErrorDialogUiModel
import com.rahim.yadino.home.presentation.model.RoutineUiModel
import com.rahim.yadino.home.presentation.ui.addDialogRoutine.component.AddRoutineDialogComponent
import com.rahim.yadino.home.presentation.ui.addDialogRoutine.component.AddRoutineDialogComponentImpl
import com.rahim.yadino.home.presentation.ui.errorDialog.component.ErrorDialogComponent
import com.rahim.yadino.home.presentation.ui.errorDialog.component.ErrorDialogComponentImpl
import com.rahim.yadino.home.presentation.ui.updateDialogRoutine.component.UpdateRoutineDialogComponent
import com.rahim.yadino.home.presentation.ui.updateDialogRoutine.component.UpdateRoutineDialogComponentImpl
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
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
    SlotNavigation<DialogSlotHomeComponent.AddRoutineDialogHome>()

  private val updateRoutineDialogNavigationSlot =
    SlotNavigation<DialogSlotHomeComponent.UpdateRoutineDialog>()

  private val errorDialogNavigationSlot =
    SlotNavigation<DialogSlotHomeComponent.ErrorDialog>()


  override val addRoutineDialogHomeScreen: Value<ChildSlot<DialogSlotHomeComponent.AddRoutineDialogHome, AddRoutineDialogComponent>> =
    childSlot(
      source = addRoutineDialogNavigationSlot,
      serializer = DialogSlotHomeComponent.AddRoutineDialogHome.serializer(),
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
      serializer = DialogSlotHomeComponent.UpdateRoutineDialog.serializer(),
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

  override val errorDialogScreen: Value<ChildSlot<DialogSlotHomeComponent.ErrorDialog, ErrorDialogComponent>> =
    childSlot(
      source = errorDialogNavigationSlot,
      serializer = DialogSlotHomeComponent.ErrorDialog.serializer(),
      handleBackButton = true,
      key = "errorDialogComponentNavigationSlot",
    ) { config, childComponentContext ->
      ErrorDialogComponentImpl(
        componentContext = childComponentContext,
        mainContext = Dispatchers.Main,
        deleteReminderUseCase = deleteReminderUseCase,
        errorDialogUiModel = config.errorDialogUiModel,
        onDismissed = errorDialogNavigationSlot::dismiss,
      )
    }

  private val scope: CoroutineScope = coroutineScope(mainContext + SupervisorJob())

  private val _state = MutableValue(RootHomeComponent.State())
  override val state: Value<RootHomeComponent.State> = _state

  private val _effect = Channel<RootHomeComponent.Effect>(Channel.BUFFERED)
  override val effect: Flow<RootHomeComponent.Effect> = _effect.receiveAsFlow()


  init {
    lifecycle.doOnCreate {
      setCurrentTime()
      getRoutines()
    }
  }

  override fun event(event: RootHomeComponent.Event) {
    when (event) {
      RootHomeComponent.Event.GetRoutines -> {
        getRoutines()
      }

      is RootHomeComponent.Event.UpdateRoutine -> {
        updateRoutine(event.routine)
      }

      is RootHomeComponent.Event.CheckedRoutine -> {
        checkedRoutine(event.routine)
      }

      is RootHomeComponent.Event.OnShowErrorDialog -> {
        showErrorDialog(event.errorDialogUiModel)
      }

      is RootHomeComponent.Event.SearchRoutine -> {
        searchRoutines(searchText = event.routineName)
      }

      is RootHomeComponent.Event.OnShowUpdateRoutineDialog -> showUpdateDialogRoutine(DialogSlotHomeComponent.UpdateRoutineDialog(event.routine))
    }
  }

  private fun setCurrentTime() {
    _state.update {
      it.copy(
        currentDate = getCurrentDateUseCase().toCurrentDatePresentationLayer(),
      )
    }
  }

  private fun searchRoutines(searchText: String) {
    scope.launch {
      if (searchText.isBlank()) {
        Timber.tag("routineSearch").d("getRoutines->$searchText")
        getRoutines()
        return@launch
      }
      searchRoutine(searchText)
    }
  }

  private fun getRoutines() {
    scope.launch {
      _state.update {
        it.copy(routines = LoadableData.Loading)
      }
      getTodayRoutinesUseCase().catch { exception ->
        _state.update {
          it.copy(routines = LoadableData.Initial)
        }
        _effect.send(RootHomeComponent.Effect.ShowToast(ErrorMessage.GET_PROCESS.toMessageUi()))
      }.collectLatest { routines ->
        Timber.tag("routineSearch").d("getNormalRoutines")
        _state.update {
          it.copy(
            routines = LoadableData.Loaded(
              routines.map { it.toRoutineUiModel() }.sortedBy {
                it.timeInMillisecond
              }.toPersistentList(),
            ),
          )
        }
      }
    }
  }

  private suspend fun searchRoutine(searchText: String) {
    _state.update {
      it.copy(routines = LoadableData.Loading)
    }
    searchRoutineUseCase(searchText).catch {
      _state.update {
        it.copy(routines = LoadableData.Initial)
      }
      _effect.send(
        RootHomeComponent.Effect.ShowToast(
          message = ErrorMessage.SEARCH_ROUTINE.toMessageUi(),
        ),
      )
    }.collectLatest { searchItems ->
      Timber.tag("routineSearch").d("searchRoutine->$searchText")
      Timber.tag("routineSearch").d("searchItems->$searchItems")
      _state.update {
        it.copy(
          routines = LoadableData.Loaded(
            searchItems.map {
              it.toRoutineUiModel()
            }.sortedBy {
              it.timeInMillisecond
            }.toPersistentList(),
          ),
        )
      }
    }
  }

  private fun showErrorDialog(errorDialogUiModel: ErrorDialogUiModel) {
    errorDialogNavigationSlot.activate(DialogSlotHomeComponent.ErrorDialog(errorDialogUiModel))
  }

  private fun updateRoutine(routineModel: RoutineUiModel) {
    Timber.tag("addRoutine").d("updateRoutine")
    _state.update {
      it.copy(routines = LoadableData.Loading)
    }
    scope.launch {
      when (val response = updateReminderUseCase(routineModel.toRoutine())) {
        is Resource.Error -> {
          _state.update {
            it.copy(routines = LoadableData.Initial)
          }
          _effect.send(
            RootHomeComponent.Effect.ShowToast(
              message = response.error.toMessageUi(),
            ),
          )
        }

        is Resource.Success -> {
          _state.update {
            it.copy(routines = LoadableData.Initial)
          }
          _effect.send(
            RootHomeComponent.Effect.ShowToast(
              message = response.data.toMessageUi(),
            ),
          )
        }
      }
    }
  }

  private fun checkedRoutine(routineModel: RoutineUiModel) {
    scope.launch {
      cancelReminderUseCase(routineModel.toRoutine())
    }
  }

  private fun showAddDialogRoutine(dialog: DialogSlotHomeComponent.AddRoutineDialogHome) {
    addRoutineDialogNavigationSlot.activate(dialog)
  }

  private fun showUpdateDialogRoutine(dialog: DialogSlotHomeComponent.UpdateRoutineDialog) {
    updateRoutineDialogNavigationSlot.activate(dialog)
  }

}
