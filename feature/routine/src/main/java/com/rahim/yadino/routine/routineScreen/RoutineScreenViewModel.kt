package com.rahim.yadino.routine.routineScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahim.yadino.Resource
import com.rahim.yadino.dateTime.DateTimeRepository
import com.rahim.yadino.di.IODispatcher
import com.rahim.yadino.routine.model.RoutineModel
import com.rahim.yadino.routine.useCase.AddReminderUseCase
import com.rahim.yadino.routine.useCase.CancelReminderUseCase
import com.rahim.yadino.routine.useCase.DeleteReminderUseCase
import com.rahim.yadino.routine.useCase.GetRemindersUseCase
import com.rahim.yadino.routine.useCase.SearchRoutineUseCase
import com.rahim.yadino.routine.useCase.UpdateReminderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
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

const val MONTH_MAX = 12
const val MONTH_MIN = 1
const val DAY_MAX = 31
const val DAY_MIN = 1

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
  ViewModel(), RoutineContract {

  private var lastYearNumber = dateTimeRepository.currentTimeYear
  private var lastMonthNumber = dateTimeRepository.currentTimeMonth
  private var lastDayNumber = dateTimeRepository.currentTimeDay

  private var mutableState = MutableStateFlow(RoutineContract.RoutineState())
  override val state: StateFlow<RoutineContract.RoutineState> = mutableState.onStart {
    setCurrentTime()
    getTimesMonth()
    getRoutines()
    getTimes()
  }.stateIn(viewModelScope, SharingStarted.Lazily, RoutineContract.RoutineState())


  private var searchNameRoutine = ""
  override fun event(event: RoutineContract.RoutineEvent) {
    when (event) {
      is RoutineContract.RoutineEvent.AddRoutine -> addRoutine(event.routine)
      is RoutineContract.RoutineEvent.CheckedRoutine -> checkedRoutine(event.routine)
      is RoutineContract.RoutineEvent.DeleteRoutine -> deleteRoutine(event.routine)
      is RoutineContract.RoutineEvent.GetRoutines -> {
        Timber.tag("routineViewModel").d("GetRoutines")
        event.run {
          updateLastTime(timeDate.yearNumber, timeDate.monthNumber, timeDate.dayNumber)
          updateDayChecked(timeDate.yearNumber, timeDate.monthNumber, timeDate.dayNumber)
          getRoutines(timeDate.yearNumber, timeDate.monthNumber, timeDate.dayNumber, searchText = searchNameRoutine)
        }
      }

      is RoutineContract.RoutineEvent.SearchRoutine -> {
        getRoutines(searchText = event.routineName)
      }

      is RoutineContract.RoutineEvent.UpdateRoutine -> updateRoutine(event.routine)
      is RoutineContract.RoutineEvent.GetAllTimes -> getTimes()
      is RoutineContract.RoutineEvent.MonthIncrease -> {
        monthIncrease(event.monthNumber, event.yearNumber) { year, month ->
          getTimesMonth(year, month)
          updateDayChecked(year, month)
          updateIndex(month, year)
        }
      }

      is RoutineContract.RoutineEvent.MonthDecrease -> {
        monthDecrease(event.monthNumber, event.yearNumber) { year, month ->
          getTimesMonth(year, month)
          updateDayChecked(year, month)
          updateIndex(month, year)
        }
      }

      is RoutineContract.RoutineEvent.JustMonthDecrease -> {
        monthDecrease(event.monthNumber, event.yearNumber) { year, month ->
          getTimesMonth(year, month)
        }
      }

      is RoutineContract.RoutineEvent.JustMonthIncrease -> {
        monthIncrease(event.monthNumber, event.yearNumber) { year, month ->
          getTimesMonth(year, month)
        }
      }

      RoutineContract.RoutineEvent.WeekDecrease -> weekDecrease()
      RoutineContract.RoutineEvent.WeekIncrease -> weekIncrease()
    }
  }

  private fun updateLastTime(yearNumber: Int, monthNumber: Int, dayNumber: Int) {
    lastYearNumber = yearNumber
    lastMonthNumber = monthNumber
    lastDayNumber = dayNumber
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

  private fun monthDecrease(month: Int, year: Int, time: (year: Int, month: Int) -> Unit) {
    viewModelScope.launch(ioDispatcher) {
      var month = month.minus(MONTH_MIN)
      var year = year
      if (month < MONTH_MIN) {
        month = MONTH_MAX
        year = year.minus(MONTH_MIN)
      }
      time(year, month)
    }
  }

  private fun monthIncrease(month: Int, year: Int, time: (year: Int, month: Int) -> Unit) {
    viewModelScope.launch(ioDispatcher) {
      var month = month.plus(MONTH_MIN)
      var year = year
      if (month > MONTH_MAX) {
        month = MONTH_MIN
        year = year.plus(MONTH_MIN)
      }
      time(year, month)
    }
  }

  private fun updateIndex(month: Int, year: Int, day: Int = DAY_MIN) {
    viewModelScope.launch(ioDispatcher) {
      val times = ArrayList(state.value.times)
      times.indexOfFirst { it.monthNumber == month && it.yearNumber == year && it.dayNumber == day }.let { index ->
        mutableState.update {
          it.copy(index = calculateIndexDay(index))
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
    yearNumber: Int = lastYearNumber,
    monthNumber: Int = lastMonthNumber,
    numberDay: Int = lastDayNumber,
    searchText: String = "",
  ) {
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
    viewModelScope.launch {
      Timber.tag("routineViewModel").d("GetRoutines")
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
        times.find { it.isChecked }?.let { currentTime ->
          updateIndex(currentTime.monthNumber, currentTime.yearNumber, currentTime.dayNumber)
        }
      }
    }
  }

  private fun getTimesMonth(yearNumber: Int = dateTimeRepository.currentTimeYear, monthNumber: Int = dateTimeRepository.currentTimeMonth) {
    viewModelScope.launch(ioDispatcher) {
      val times = dateTimeRepository.getTimesMonth(yearNumber, monthNumber).toCollection(ArrayList())
      val isCheckedTime = times.find { it.isChecked || it.isToday }
      if (isCheckedTime == null) {
        val time = times.indexOfFirst { it.dayNumber == DAY_MIN }
        times[time] = times.first { it.dayNumber == DAY_MIN }.copy(isChecked = true)
      }
      mutableState.update {
        it.copy(timesMonth = times, errorMessage = null)
      }
    }
  }

  private fun calculateIndexDay(index: Int) = index.minus(index % 7)

  private fun updateDayChecked(yearNumber: Int, monthNumber: Int, day: Int = DAY_MIN) {
    viewModelScope.launch {
      dateTimeRepository.updateDayToToday(day, yearNumber, monthNumber)
      mutableState.update {
        it.copy(currentMonth = monthNumber, currentYear = yearNumber, currentDay = day)
      }
    }
  }
}
