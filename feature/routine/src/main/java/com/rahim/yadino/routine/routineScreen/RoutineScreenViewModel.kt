package com.rahim.yadino.routine.routineScreen

import androidx.lifecycle.viewModelScope
import com.rahim.yadino.Resource
import com.rahim.yadino.base.BaseViewModel
import com.rahim.yadino.dateTime.DateTimeRepository
import com.rahim.yadino.di.IODispatcher
import com.rahim.yadino.model.RoutineModel
import com.rahim.yadino.model.TimeDate
import com.rahim.yadino.routine.useCase.AddReminderUseCase
import com.rahim.yadino.routine.useCase.CancelReminderUseCase
import com.rahim.yadino.routine.useCase.DeleteReminderUseCase
import com.rahim.yadino.routine.useCase.GetRemindersUseCase
import com.rahim.yadino.routine.useCase.SearchRoutineUseCase
import com.rahim.yadino.routine.useCase.UpdateReminderUseCase
import com.rahim.yadino.sharedPreferences.SharedPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
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
  @IODispatcher
  private val ioDispatcher: CoroutineDispatcher,
) :
  BaseViewModel(), RoutineContract {

  private var lastYearNumber = dateTimeRepository.currentTimeYear
  private var lastMonthNumber = dateTimeRepository.currentTimeMonth
  private var lastDayNumber = dateTimeRepository.currentTimeDay

  private val mutableState = MutableStateFlow(RoutineContract.RoutineState())
  override val state: StateFlow<RoutineContract.RoutineState> = mutableState.onStart {
    setCurrentTime()
    calculateCurrentIndexDay()
    getTimesMonth()
    getRoutines()
    getTimes()
  }.stateIn(viewModelScope, SharingStarted.Lazily, RoutineContract.RoutineState())

  override fun event(event: RoutineContract.RoutineEvent) {
    when (event) {
      is RoutineContract.RoutineEvent.AddRoutine -> addRoutine(event.routine)
      is RoutineContract.RoutineEvent.CheckedRoutine -> checkedRoutine(event.routine)
      is RoutineContract.RoutineEvent.DeleteRoutine -> deleteRoutine(event.routine)
      is RoutineContract.RoutineEvent.GetRoutines -> {
        updateDayChecked(event.timeDate)
        getRoutines(event.timeDate.yearNumber, event.timeDate.monthNumber, event.timeDate.dayNumber)
      }

      is RoutineContract.RoutineEvent.SearchRoutine -> searchRoutine(event.routineName)
      is RoutineContract.RoutineEvent.UpdateRoutine -> updateRoutine(event.routine)
      is RoutineContract.RoutineEvent.GetTimesMonth -> getTimesMonth(event.yearNumber, event.monthNumber)
      is RoutineContract.RoutineEvent.GetAllTimes -> getTimes()
      RoutineContract.RoutineEvent.MonthIncrease -> monthIncrease()
      RoutineContract.RoutineEvent.MonthDecrease -> monthDecrease()
      RoutineContract.RoutineEvent.WeekDecrease -> weekDecrease()
      RoutineContract.RoutineEvent.WeekIncrease -> weekIncrease()
    }
  }

  private fun weekIncrease() {
    val times = state.value.times
    val index = state.value.index
    val updateIndex = if (times.size <= index + 7) {
      times.size
    } else if (index == -1) {
      index + 8
    } else {
      index + 7
    }
    mutableState.update {
      it.copy(index = updateIndex)
    }
  }

  private fun weekDecrease() {
    val index = state.value.index
    val updateIndex = if (index <= 6) {
      0
    } else {
      index - 7
    }
    mutableState.update {
      it.copy(index = updateIndex)
    }
  }

  private fun monthDecrease() {
    viewModelScope.launch(ioDispatcher) {
      var month = state.value.currentMonth.minus(1)
      var year = state.value.currentYear
      if (month < 1) {
        month = 12
        year = state.value.currentYear.minus(1)
      }
      val time =
        state.value.times.find { it.monthNumber == month && it.yearNumber == year && it.dayNumber == 1 }
      if (time != null) {
        val index = state.value.times.indexOf(time)
        mutableState.update {
          it.copy(index = calculateIndexDay(index), currentMonth = month, currentYear = year)
        }
      }
    }
  }

  private fun monthIncrease() {
    viewModelScope.launch(ioDispatcher) {
      var month = state.value.currentMonth.plus(1)
      var year = state.value.currentYear
      if (month > 12) {
        month = 1
        year = state.value.currentYear.plus(1)
      }
      val time =
        state.value.times.find { it.monthNumber == month && it.yearNumber == year && it.dayNumber == 1 }
      if (time != null) {
        val index = state.value.times.indexOf(time)
        mutableState.update {
          it.copy(index = calculateIndexDay(index), currentMonth = month, currentYear = year)
        }
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

  private fun getRoutines(
    yearNumber: Int = dateTimeRepository.currentTimeYear,
    monthNumber: Int = dateTimeRepository.currentTimeMonth,
    numberDay: Int = dateTimeRepository.currentTimeDay,
  ) {
    viewModelScope.launch {
      lastYearNumber = yearNumber
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

  private fun calculateCurrentIndexDay(currentIndex: Int, previousIndex: Int): Int {
    return if (currentIndex > previousIndex) {
      previousIndex + 7
    } else {
      if (previousIndex - 7 < 0) {
        previousIndex
      } else {
        previousIndex - 7
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
          it.copy(
            times = times, errorMessage = null,
          )
        }
      }
    }
  }

  private fun getTimesMonth(yearNumber: Int = dateTimeRepository.currentTimeYear, monthNumber: Int = dateTimeRepository.currentTimeMonth) {
    viewModelScope.launch {
      dateTimeRepository.getTimesMonth(yearNumber, monthNumber).catch {}.collectLatest { times ->
        mutableState.update {
          it.copy(timesMonth = times, errorMessage = null)
        }
      }
    }
  }

  private fun calculateCurrentIndexDay() {
    viewModelScope.launch(ioDispatcher) {
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

  private fun calculateIndexDay(index: Int) = index.minus(index % 7)

  private fun updateDayChecked(timeDate: TimeDate) {
    viewModelScope.launch(ioDispatcher) {
      val times = ArrayList(state.value.times)
      val time = times.find { it.isChecked }
      val indexPreviousTime = times.indexOf(time)
      val indexNewTime = times.indexOf(timeDate)
      times[indexPreviousTime] = time?.copy(isChecked = false)
      times[indexNewTime] = timeDate.copy(isChecked = true)
      mutableState.update {
        it.copy(times = times)
      }
    }
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
}
