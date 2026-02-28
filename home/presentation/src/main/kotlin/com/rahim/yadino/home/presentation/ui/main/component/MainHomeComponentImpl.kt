package com.rahim.yadino.home.presentation.ui.main.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.rahim.yadino.base.LoadableData
import com.rahim.yadino.base.Resource
import com.rahim.yadino.base.toMessageUi
import com.rahim.yadino.enums.message.error.ErrorMessage
import com.rahim.yadino.home.domain.useCase.CancelReminderUseCase
import com.rahim.yadino.home.domain.useCase.GetCurrentDateUseCase
import com.rahim.yadino.home.domain.useCase.GetTodayRoutinesUseCase
import com.rahim.yadino.home.domain.useCase.SearchRoutineUseCase
import com.rahim.yadino.home.domain.useCase.UpdateReminderUseCase
import com.rahim.yadino.home.presentation.mapper.toCurrentDatePresentationLayer
import com.rahim.yadino.home.presentation.mapper.toRoutine
import com.rahim.yadino.home.presentation.mapper.toRoutineUiModel
import com.rahim.yadino.home.presentation.model.ErrorDialogRemoveUiModel
import com.rahim.yadino.home.presentation.model.RoutineUiModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class MainHomeComponentImpl(
  componentContext: ComponentContext,
  mainContext: CoroutineContext,
  private val cancelReminderUseCase: CancelReminderUseCase,
  private val getTodayRoutinesUseCase: GetTodayRoutinesUseCase,
  private val searchRoutineUseCase: SearchRoutineUseCase,
  private val getCurrentDateUseCase: GetCurrentDateUseCase,
  private val updateReminderUseCase: UpdateReminderUseCase,
  private val showErrorRemoveRoutineDialog: (ErrorDialogRemoveUiModel) -> Unit,
  private val showUpdateRoutineDialog: (RoutineUiModel) -> Unit,
) : MainHomeComponent, ComponentContext by componentContext {

  private val scope: CoroutineScope = coroutineScope(mainContext + SupervisorJob())

  private val _state = MutableValue(MainHomeComponent.State())
  override val state: Value<MainHomeComponent.State> = _state

  private val _effect = Channel<MainHomeComponent.Effect>(Channel.BUFFERED)
  override val effects: Flow<MainHomeComponent.Effect> = _effect.receiveAsFlow()


  init {
    lifecycle.doOnCreate {
      setCurrentTime()
      getRoutines()
    }
  }

  override fun onEvent(event: MainHomeComponent.Event) {
    when (event) {
      MainHomeComponent.Event.GetRoutines -> {
        getRoutines()
      }

      is MainHomeComponent.Event.UpdateRoutine -> {
        updateRoutine(event.routine)
      }

      is MainHomeComponent.Event.CheckedRoutine -> {
        checkedRoutine(event.routine)
      }

      is MainHomeComponent.Event.SearchRoutine -> {
        searchRoutines(searchText = event.routineName)
      }

      is MainHomeComponent.Event.ShowErrorDialogRemoveRoutine -> showErrorRemoveRoutineDialog(event.errorDialogModel)
      is MainHomeComponent.Event.ShowUpdateRoutineDialog -> showUpdateRoutineDialog(event.updateRoutine)
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
        _effect.send(MainHomeComponent.Effect.ShowToast(ErrorMessage.GET_PROCESS.toMessageUi()))
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

  private fun checkedRoutine(routineModel: RoutineUiModel) {
    scope.launch {
      cancelReminderUseCase(routineModel.toRoutine())
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
        MainHomeComponent.Effect.ShowToast(
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
            MainHomeComponent.Effect.ShowToast(
              message = response.error.toMessageUi(),
            ),
          )
        }

        is Resource.Success -> {
          _state.update {
            it.copy(routines = LoadableData.Initial)
          }
          _effect.send(
            MainHomeComponent.Effect.ShowToast(
              message = response.data.toMessageUi(),
            ),
          )
        }
      }
    }
  }
}
