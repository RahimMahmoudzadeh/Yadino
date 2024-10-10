package com.rahim.yadino.routine.routineScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahim.yadino.Resource
import com.rahim.yadino.collectWithoutHistory
import com.rahim.yadino.model.TimeDate
import com.rahim.yadino.dateTime.DateTimeRepository
import com.rahim.yadino.model.RoutineModel
import com.rahim.yadino.routine.useCase.AddReminderUseCase
import com.rahim.yadino.routine.useCase.CancelReminderUseCase
import com.rahim.yadino.routine.useCase.DeleteReminderUseCase
import com.rahim.yadino.routine.useCase.GetRemindersUseCase
import com.rahim.yadino.routine.useCase.SearchRoutineUseCase
import com.rahim.yadino.routine.useCase.UpdateReminderUseCase
import com.rahim.yadino.sharedPreferences.SharedPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RoutineScreenViewModel @Inject constructor(
  private val addReminderUseCase: AddReminderUseCase,
  private val updateReminderUseCase: UpdateReminderUseCase,
  private val cancelReminderUseCase: CancelReminderUseCase,
  private val deleteReminderUseCase: DeleteReminderUseCase,
  private val getRemindersUseCase: GetRemindersUseCase,
  private val searchRoutineUseCase: SearchRoutineUseCase,
  private val dateTimeRepository: DateTimeRepository,
  private val sharedPreferencesRepository: SharedPreferencesRepository,
) :
  ViewModel(), RoutineContract {

  private var lastYearNumber = dateTimeRepository.currentTimeYer
  private var lastMonthNumber = dateTimeRepository.currentTimeMonth
  private var lastDayNumber = dateTimeRepository.currentTimeDay
  val currentYear = dateTimeRepository.currentTimeYer
  val currentMonth = dateTimeRepository.currentTimeMonth
  val currentDay = dateTimeRepository.currentTimeDay

  private val mutableState = MutableStateFlow(RoutineContract.RoutineState())
  override val state: StateFlow<RoutineContract.RoutineState> = mutableState.onStart {
    calculateIndexDay()
    getTimesMonth()
    getRoutines()
    getTimes()
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), RoutineContract.RoutineState())

  override fun event(event: RoutineContract.RoutineEvent) {
    when (event) {
      is RoutineContract.RoutineEvent.AddRoutine -> addRoutine(event.routine)
      is RoutineContract.RoutineEvent.CheckedRoutine -> checkedRoutine(event.routine)
      is RoutineContract.RoutineEvent.DeleteRoutine -> deleteRoutine(event.routine)
      is RoutineContract.RoutineEvent.GetRoutines -> getRoutines(event.yearNumber, event.monthNumber, event.numberDay)
      is RoutineContract.RoutineEvent.SearchRoutine -> searchRoutine(event.routineName)
      is RoutineContract.RoutineEvent.ShowSampleRoutines -> showSampleRoutine()
      is RoutineContract.RoutineEvent.UpdateRoutine -> updateRoutine(event.routine)
      is RoutineContract.RoutineEvent.GetTimesMonth -> getTimesMonth(event.yearNumber, event.monthNumber)
      is RoutineContract.RoutineEvent.GetAllTimes -> getTimes()
      is RoutineContract.RoutineEvent.SetDayIndex -> setDayIndex(event.index)
    }
  }

  private fun getRoutines(
    yerNumber: Int = dateTimeRepository.currentTimeYer,
    monthNumber: Int = dateTimeRepository.currentTimeMonth,
    numberDay: Int = dateTimeRepository.currentTimeDay,
  ) {
    viewModelScope.launch {
      lastYearNumber = yerNumber
      lastMonthNumber = monthNumber
      lastDayNumber = numberDay
      getRemindersUseCase.invoke(lastMonthNumber, lastDayNumber, lastYearNumber)
        .catch {}.collectLatest { routines ->
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
  }

  private fun deleteRoutine(routineModel: RoutineModel) {
    viewModelScope.launch {
      deleteReminderUseCase(routineModel)
    }
  }

  private fun updateRoutine(routineModel: RoutineModel) {
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

  private fun getTimes() {
    viewModelScope.launch {
      dateTimeRepository.getTimes().catch {}.collect { times ->
        mutableState.update {
          it.copy(times = times, errorMessage = null)
        }
      }
    }
  }

  private fun getTimesMonth(yearNumber: Int = dateTimeRepository.currentTimeYer, monthNumber: Int = dateTimeRepository.currentTimeMonth) {
    viewModelScope.launch {
      dateTimeRepository.getTimesMonth(yearNumber, monthNumber).catch {}.collectLatest { times ->
        mutableState.update {
          it.copy(timesMonth = times, errorMessage = null)
        }
      }
    }
  }

  private fun calculateIndexDay() {
    viewModelScope.launch {
      var timesSize = 0
      dateTimeRepository.getTimes().distinctUntilChanged().catch {}.collectLatest { times ->
        if (timesSize != times.size) {
          timesSize = times.size
          val currentTime = times.find { it.isToday }
          val indexCurrentDay = times.indexOf(currentTime)
          mutableState.update {
            it.copy(index = calculateIndexDay(indexCurrentDay), errorMessage = null)
          }
        }
      }
    }
  }

  private fun calculateIndexDay(index: Int): Int {
    var indexPosition = 0
    while (true) {
      indexPosition += 7
      if (indexPosition > index) {
        indexPosition -= 7
        break
      }
    }
    return indexPosition
  }

  private fun setDayIndex(index: Int) {
    mutableState.update { it.copy(index = index, errorMessage = null) }
  }

  private fun searchRoutine(searchText: String) {
    viewModelScope.launch {
      if (searchText.isNotEmpty()) {
        Timber.tag("searchRoutine").d("searchText:$searchText")
        val searchItems = searchRoutineUseCase(searchText, lastYearNumber, lastMonthNumber, lastDayNumber)
        mutableState.update {
          it.copy(
            routines = searchItems.sortedBy {
              it.timeHours?.replace(":", "")?.toInt()
            },
            errorMessage = null,
          )
        }
      } else {
        getRoutines()
      }
    }
  }

  private fun showSampleRoutine(isShow: Boolean = true) {
    viewModelScope.launch {
      sharedPreferencesRepository.isShowSampleRoutine(isShow)
    }
  }
}
