package com.rahim.yadino.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahim.home.domain.useCase.AddReminderUseCase
import com.rahim.home.domain.useCase.CancelReminderUseCase
import com.rahim.home.domain.useCase.DeleteReminderUseCase
import com.rahim.home.domain.useCase.GetCurrentDateUseCase
import com.rahim.home.domain.useCase.GetTodayRoutinesUseCase
import com.rahim.home.domain.useCase.SearchRoutineUseCase
import com.rahim.home.domain.useCase.UpdateReminderUseCase
import com.rahim.yadino.base.LoadableData
import com.rahim.yadino.base.Resource
import com.rahim.yadino.enums.error.ErrorMessageCode
import com.rahim.yadino.home.presentation.mapper.toCurrentDatePresentationLayer
import com.rahim.yadino.home.presentation.mapper.toRoutine
import com.rahim.yadino.home.presentation.mapper.toRoutineUiModel
import com.rahim.yadino.home.presentation.model.RoutineUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val addReminderUseCase: AddReminderUseCase,
  private val updateReminderUseCase: UpdateReminderUseCase,
  private val cancelReminderUseCase: CancelReminderUseCase,
  private val deleteReminderUseCase: DeleteReminderUseCase,
  private val getTodayRoutinesUseCase: GetTodayRoutinesUseCase,
  private val searchRoutineUseCase: SearchRoutineUseCase,
  private val getCurrentDateUseCase: GetCurrentDateUseCase,
) : ViewModel(), HomeContract {

  private val _state = MutableStateFlow(HomeContract.HomeState())
  override val state: StateFlow<HomeContract.HomeState> = _state.onStart {
    setCurrentTime()
    getRoutines()
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HomeContract.HomeState())

  override fun event(event: HomeContract.HomeEvent) {
    when (event) {
      HomeContract.HomeEvent.GetRoutines -> {
        getRoutines()
      }

      is HomeContract.HomeEvent.AddRoutine -> {
        addRoutine(event.routine)
      }

      is HomeContract.HomeEvent.UpdateRoutine -> {
        updateRoutine(event.routine)
      }

      is HomeContract.HomeEvent.CheckedRoutine -> {
        checkedRoutine(event.routine)
      }

      is HomeContract.HomeEvent.DeleteRoutine -> {
        deleteRoutine(event.routine)
      }

      is HomeContract.HomeEvent.SearchRoutine -> {
        searchRoutines(searchText = event.routineName)
      }
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
    viewModelScope.launch {
      if (searchText.isBlank()) {
        Timber.tag("routineSearch").d("getRoutines->$searchText")
        getRoutines()
        return@launch
      }
      searchRoutine(searchText)
    }
  }

  private fun getRoutines() {
    viewModelScope.launch {
      _state.update {
        it.copy(routines = LoadableData.Loading)
      }
      getTodayRoutinesUseCase().catch { exception ->
        _state.update {
          it.copy(
            routines = LoadableData.Error(error = ErrorMessageCode.ERROR_GET_PROCESS),
          )
        }
      }.collectLatest { routines ->
        Timber.tag("routineSearch").d("getNormalRoutines")
        _state.update {
          it.copy(
            routines = LoadableData.Loaded(
              routines.map { it.toRoutineUiModel() }.sortedBy {
                it.timeInMillisecond
              }.toPersistentList(),
            ),
            errorMessage = null,
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
        it.copy(
          routines = LoadableData.Error(error = ErrorMessageCode.ERROR_GET_PROCESS),
        )
      }
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
          errorMessage = null,
        )
      }
    }
  }

  private fun deleteRoutine(routineModel: RoutineUiModel) {
    viewModelScope.launch {
      deleteReminderUseCase(routineModel.toRoutine())
    }
  }

  private fun updateRoutine(routineModel: RoutineUiModel) {
    Timber.tag("addRoutine").d("updateRoutine")
    viewModelScope.launch {
      val response = updateReminderUseCase(routineModel.toRoutine())
      when (response) {
        is Resource.Error -> {
          _state.update { state ->
            state.copy(
              errorMessage = response.error,
            )
          }
        }

        is Resource.Success -> {}
      }
    }
  }

  private fun checkedRoutine(routineModel: RoutineUiModel) {
    viewModelScope.launch {
      cancelReminderUseCase(routineModel.toRoutine())
    }
  }

  private fun addRoutine(routineModel: RoutineUiModel) {
    viewModelScope.launch {
      Timber.tag("addRoutine").d("addRoutine")
      val response = addReminderUseCase(routineModel.toRoutine())
      when (response) {
        is Resource.Error -> {
          _state.update { state ->
            state.copy(
              errorMessage = response.error,
            )
          }
        }

        is Resource.Success -> {}
      }
    }
  }
}
