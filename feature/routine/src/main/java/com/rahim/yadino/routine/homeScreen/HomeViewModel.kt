package com.rahim.yadino.routine.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahim.yadino.Resource
import com.rahim.yadino.dateTime.DateTimeRepository
import com.rahim.yadino.routine.model.RoutineModel
import com.rahim.yadino.routine.useCase.AddReminderUseCase
import com.rahim.yadino.routine.useCase.CancelReminderUseCase
import com.rahim.yadino.routine.useCase.DeleteReminderUseCase
import com.rahim.yadino.routine.useCase.GetRemindersUseCase
import com.rahim.yadino.routine.useCase.SearchRoutineUseCase
import com.rahim.yadino.routine.useCase.UpdateReminderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
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
  private val getRemindersUseCase: GetRemindersUseCase,
  private val searchRoutineUseCase: SearchRoutineUseCase,
  private val dateTimeRepository: DateTimeRepository,
) : ViewModel(), HomeContract {

  private val mutableState = MutableStateFlow(HomeContract.HomeState())
  override val state: StateFlow<HomeContract.HomeState> = mutableState.onStart {
    setCurrentTime()
    getRoutines(
      yearNumber = dateTimeRepository.currentTimeYear,
      monthNumber = dateTimeRepository.currentTimeMonth,
      numberDay = dateTimeRepository.currentTimeDay,
    )
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
        getRoutines(searchText = event.routineName)
      }
    }
  }

  private fun setCurrentTime() {
    mutableState.update {
      it.copy(
        currentDay = dateTimeRepository.currentTimeDay,
        currentMonth = dateTimeRepository.currentTimeMonth,
        currentYear = dateTimeRepository.currentTimeYear,
      )
    }
  }
  private var searchNameRoutine = ""
  private fun getRoutines(yearNumber: Int = dateTimeRepository.currentTimeYear, monthNumber: Int = dateTimeRepository.currentTimeMonth, numberDay: Int = dateTimeRepository.currentTimeDay, searchText: String = "") {
    viewModelScope.launch {
      searchNameRoutine = searchText
      if (searchText.isNotBlank()) {
        Timber.tag("routineSearch").d("getRoutines->$searchNameRoutine")
        searchRoutine(searchNameRoutine, this, yearNumber, monthNumber, numberDay)
      } else {
        getNormalRoutines(this, yearNumber, monthNumber, numberDay)
      }
    }
  }

  private suspend fun getNormalRoutines(scope: CoroutineScope, yearNumber: Int, monthNumber: Int, numberDay: Int) {
    getRemindersUseCase.invoke(monthNumber, numberDay, yearNumber, scope).collectLatest { routines ->
      Timber.tag("routineSearch").d("getNormalRoutines")
      mutableState.update {
        it.copy(
          routines =
          routines.sortedBy {
            it.timeHours?.replace(":", "")?.toInt()
          },
          routineLoading = false,
          errorMessage = null,
        )
      }
    }
  }

  private suspend fun searchRoutine(searchText: String, scope: CoroutineScope, yearNumber: Int, monthNumber: Int, numberDay: Int) {
    searchRoutineUseCase.invoke(searchText, yearNumber, monthNumber, numberDay, scope).collectLatest { searchItems ->
      Timber.tag("routineSearch").d("searchRoutine->$searchText")
      Timber.tag("routineSearch").d("searchItems->$searchItems")
      mutableState.update {
        it.copy(
          searchRoutines = searchItems.sortedBy {
            it.timeHours?.replace(":", "")?.toInt()
          },
          routineLoading = false,
          errorMessage = null,
        )
      }
    }
  }

  private fun deleteRoutine(routineModel: RoutineModel) {
    viewModelScope.launch {
      deleteReminderUseCase(routineModel)
    }
  }

  private fun updateRoutine(routineModel: RoutineModel) {
    Timber.tag("addRoutine").d("updateRoutine")
    viewModelScope.launch {
      val response = updateReminderUseCase(routineModel)
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

  private fun checkedRoutine(routineModel: RoutineModel) {
    viewModelScope.launch {
      cancelReminderUseCase(routineModel)
    }
  }

  private fun addRoutine(routineModel: RoutineModel) {
    viewModelScope.launch {
      Timber.tag("addRoutine").d("addRoutine")
      val response = addReminderUseCase(routineModel)
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
