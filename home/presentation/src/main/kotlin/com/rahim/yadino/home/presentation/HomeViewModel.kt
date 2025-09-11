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
import com.rahim.yadino.Resource
import com.rahim.yadino.home.presentation.mapper.toCurrentDatePresentationLayer
import com.rahim.yadino.home.presentation.mapper.toRoutineHomeDomainLayer
import com.rahim.yadino.home.presentation.mapper.toRoutineHomePresentationLayer
import com.rahim.yadino.home.presentation.model.RoutineHomePresentationLayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
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

  private val mutableState = MutableStateFlow(HomeContract.HomeState())
  override val state: StateFlow<HomeContract.HomeState> = mutableState.onStart {
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
    mutableState.update {
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
      getTodayRoutinesUseCase().collectLatest { routines ->
        Timber.tag("routineSearch").d("getNormalRoutines")
        mutableState.update {
          it.copy(
            routines =
              routines.map { it.toRoutineHomePresentationLayer() }.sortedBy {
                it.timeInMillisecond
              },
            routineLoading = false,
            errorMessage = null,
          )
        }
      }
    }
  }

  private suspend fun searchRoutine(searchText: String) {
    searchRoutineUseCase.invoke(searchText).collectLatest { searchItems ->
      Timber.tag("routineSearch").d("searchRoutine->$searchText")
      Timber.tag("routineSearch").d("searchItems->$searchItems")
      mutableState.update {
        it.copy(
          searchRoutines = searchItems.map {
            it.toRoutineHomePresentationLayer()
          }.sortedBy {
            it.timeInMillisecond
          },
          routineLoading = false,
          errorMessage = null,
        )
      }
    }
  }

  private fun deleteRoutine(routineModel: RoutineHomePresentationLayer) {
    viewModelScope.launch {
      deleteReminderUseCase(routineModel.toRoutineHomeDomainLayer())
    }
  }

  private fun updateRoutine(routineModel: RoutineHomePresentationLayer) {
    Timber.tag("addRoutine").d("updateRoutine")
    viewModelScope.launch {
      val response = updateReminderUseCase(routineModel.toRoutineHomeDomainLayer())
      when (response) {
        is Resource.Error -> {
          mutableState.update { state ->
            state.copy(
              errorMessage = response.message,
            )
          }
        }

        is Resource.Success -> {}
      }
    }
  }

  private fun checkedRoutine(routineModel: RoutineHomePresentationLayer) {
    viewModelScope.launch {
      cancelReminderUseCase(routineModel.toRoutineHomeDomainLayer())
    }
  }

  private fun addRoutine(routineModel: RoutineHomePresentationLayer) {
    viewModelScope.launch {
      Timber.tag("addRoutine").d("addRoutine")
      val response = addReminderUseCase(routineModel.toRoutineHomeDomainLayer())
      when (response) {
        is Resource.Error -> {
          mutableState.update { state ->
            state.copy(
              errorMessage = response.message,
            )
          }
        }

        is Resource.Success -> {}
      }
    }
  }
}
