package com.rahim.yadino.home.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.rahim.yadino.home.domain.useCase.CancelReminderUseCase
import com.rahim.yadino.home.domain.useCase.DeleteReminderUseCase
import com.rahim.yadino.home.domain.useCase.GetCurrentDateUseCase
import com.rahim.yadino.home.domain.useCase.GetTodayRoutinesUseCase
import com.rahim.yadino.home.domain.useCase.SearchRoutineUseCase
import com.rahim.yadino.home.domain.useCase.UpdateReminderUseCase
import com.rahim.yadino.base.LoadableData
import com.rahim.yadino.base.Resource
import com.rahim.yadino.enums.message.MessageCode
import com.rahim.yadino.home.presentation.mapper.toCurrentDatePresentationLayer
import com.rahim.yadino.home.presentation.mapper.toRoutine
import com.rahim.yadino.home.presentation.mapper.toRoutineUiModel
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

class HomeComponentImpl(
  componentContext: ComponentContext,
  mainContext: CoroutineContext,
  private val updateReminderUseCase: UpdateReminderUseCase,
  private val cancelReminderUseCase: CancelReminderUseCase,
  private val deleteReminderUseCase: DeleteReminderUseCase,
  private val getTodayRoutinesUseCase: GetTodayRoutinesUseCase,
  private val searchRoutineUseCase: SearchRoutineUseCase,
  private val getCurrentDateUseCase: GetCurrentDateUseCase,
  private val onShowUpdateRoutineDialog: (RoutineUiModel) -> Unit,
) : HomeComponent, ComponentContext by componentContext {

  private val scope: CoroutineScope = coroutineScope(mainContext + SupervisorJob())

  private val _state = MutableValue(HomeComponent.State())
  override val state: Value<HomeComponent.State> = _state

  private val _effect = Channel<HomeComponent.Effect>(Channel.BUFFERED)
  override val effect: Flow<HomeComponent.Effect> = _effect.receiveAsFlow()

  init {
    lifecycle.doOnCreate {
      setCurrentTime()
      getRoutines()
    }
  }

  override fun event(event: HomeComponent.Event) {
    when (event) {
      HomeComponent.Event.GetRoutines -> {
        getRoutines()
      }

      is HomeComponent.Event.UpdateRoutine -> {
        updateRoutine(event.routine)
      }

      is HomeComponent.Event.CheckedRoutine -> {
        checkedRoutine(event.routine)
      }

      is HomeComponent.Event.DeleteRoutine -> {
        deleteRoutine(event.routine)
      }

      is HomeComponent.Event.SearchRoutine -> {
        searchRoutines(searchText = event.routineName)
      }

      is HomeComponent.Event.OnShowUpdateRoutineDialog -> onShowUpdateRoutineDialog(event.routine)
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
        _effect.send(HomeComponent.Effect.ShowToast(MessageCode.ERROR_GET_PROCESS))
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
        HomeComponent.Effect.ShowToast(
          message = MessageCode.ERROR_SEARCH_ROUTINE,
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

  private fun deleteRoutine(routineModel: RoutineUiModel) {
    scope.launch {
      deleteReminderUseCase(routineModel.toRoutine())
    }
  }

  private fun updateRoutine(routineModel: RoutineUiModel) {
    Timber.tag("addRoutine").d("updateRoutine")
    _state.update {
      it.copy(routines = LoadableData.Loading)
    }
    scope.launch {
      val response = updateReminderUseCase(routineModel.toRoutine())
      when (response) {
        is Resource.Error -> {
          _state.update {
            it.copy(routines = LoadableData.Initial)
          }
          _effect.send(
            HomeComponent.Effect.ShowToast(
              message = response.error,
            ),
          )
        }

        is Resource.Success -> {
          _state.update {
            it.copy(routines = LoadableData.Initial)
          }
          _effect.send(
            HomeComponent.Effect.ShowToast(
              message = response.data,
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
}
