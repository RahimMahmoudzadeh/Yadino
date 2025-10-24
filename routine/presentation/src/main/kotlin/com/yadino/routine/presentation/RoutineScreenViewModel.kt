package com.yadino.routine.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahim.yadino.Constants.DAY_MIN
import com.rahim.yadino.Constants.MONTH_MAX
import com.rahim.yadino.Constants.MONTH_MIN
import com.rahim.yadino.base.LoadableData
import com.rahim.yadino.base.Resource
import com.rahim.yadino.core.timeDate.repo.DateTimeRepository
import com.yadino.routine.domain.useCase.AddReminderUseCase
import com.yadino.routine.domain.useCase.CancelReminderUseCase
import com.yadino.routine.domain.useCase.DeleteReminderUseCase
import com.yadino.routine.domain.useCase.GetRemindersUseCase
import com.yadino.routine.domain.useCase.SearchRoutineUseCase
import com.yadino.routine.domain.useCase.UpdateReminderUseCase
import com.yadino.routine.presentation.mapper.toRoutine
import com.yadino.routine.presentation.mapper.toRoutineUiModel
import com.yadino.routine.presentation.mapper.toTimeDateUiModel
import com.yadino.routine.presentation.model.IncreaseDecrease
import com.yadino.routine.presentation.model.RoutineUiModel
import kotlinx.collections.immutable.toPersistentList
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

class RoutineScreenViewModel(
  private val addReminderUseCase: AddReminderUseCase,
  private val updateReminderUseCase: UpdateReminderUseCase,
  private val cancelReminderUseCase: CancelReminderUseCase,
  private val deleteReminderUseCase: DeleteReminderUseCase,
  private val getRemindersUseCase: GetRemindersUseCase,
  private val searchRoutineUseCase: SearchRoutineUseCase,
  private val dateTimeRepository: DateTimeRepository,
  private val ioDispatcher: CoroutineDispatcher,
) :
  ViewModel(), RoutineContract {

  private var lastYearNumber = dateTimeRepository.currentTimeYear
  private var lastMonthNumber = dateTimeRepository.currentTimeMonth
  private var lastDayNumber = dateTimeRepository.currentTimeDay

  private var _state = MutableStateFlow(RoutineContract.State())
  override val state: StateFlow<RoutineContract.State> = _state.onStart {
    setCurrentTime()
    getTimesMonth()
    getRoutines()
    getTimes()
  }.stateIn(viewModelScope, SharingStarted.Lazily, RoutineContract.State())

  private var searchNameRoutine = ""
  override fun event(event: RoutineContract.Event) {
    when (event) {
      is RoutineContract.Event.AddRoutine -> addRoutine(event.routine)
      is RoutineContract.Event.CheckedRoutine -> checkedRoutine(event.routine)
      is RoutineContract.Event.DeleteRoutine -> deleteRoutine(event.routine)
      is RoutineContract.Event.GetRoutines -> {
        event.run {
          updateLastTime(timeDate.yearNumber, timeDate.monthNumber, timeDate.dayNumber)
          updateDayChecked(timeDate.yearNumber, timeDate.monthNumber, timeDate.dayNumber)
          getRoutines(timeDate.yearNumber, timeDate.monthNumber, timeDate.dayNumber, searchText = searchNameRoutine)
        }
      }

      is RoutineContract.Event.SearchRoutineByName -> {
        getRoutines(searchText = event.routineName)
      }

      is RoutineContract.Event.UpdateRoutine -> updateRoutine(event.routine)
      is RoutineContract.Event.GetAllTimes -> getTimes()
      is RoutineContract.Event.MonthChange -> checkMonthIncreaseOrDecrease(event.yearNumber, event.monthNumber, event.increaseDecrease)
      is RoutineContract.Event.WeekChange -> checkWeekIncreaseOrDecrease(event.increaseDecrease)
      is RoutineContract.Event.DialogMonthChange -> {
        checkDialogMonthChange(event.monthNumber, event.yearNumber, event.increaseDecrease)
      }
    }
  }

  private fun checkDialogMonthChange(monthNumber: Int, yearNumber: Int, increaseDecrease: IncreaseDecrease) {
    when (increaseDecrease) {
      IncreaseDecrease.INCREASE -> monthIncrease(monthNumber, yearNumber) { year, month ->
        getTimesMonth(year, month)
      }

      IncreaseDecrease.DECREASE ->
        monthDecrease(monthNumber, yearNumber) { year, month ->
          getTimesMonth(year, month)
        }
    }
  }

  private fun checkWeekIncreaseOrDecrease(increaseDecrease: IncreaseDecrease) {
    when (increaseDecrease) {
      IncreaseDecrease.INCREASE -> weekIncrease()
      IncreaseDecrease.DECREASE -> weekDecrease()
    }
  }

  private fun checkMonthIncreaseOrDecrease(yearNumber: Int, monthNumber: Int, increaseDecrease: IncreaseDecrease) {
    when (increaseDecrease) {
      IncreaseDecrease.INCREASE -> {
        monthIncrease(month = monthNumber, year = yearNumber) { year, month ->
          getTimesMonth(year, month)
          updateDayChecked(year, month)
          updateIndex(month, year)
        }
      }

      IncreaseDecrease.DECREASE -> {
        monthDecrease(month = monthNumber, year = yearNumber) { year, month ->
          getTimesMonth(year, month)
          updateDayChecked(year, month)
          updateIndex(month, year)
        }
      }
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
    _state.update {
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
    _state.update {
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
        _state.update {
          it.copy(index = calculateIndexDay(index))
        }
      }
    }
  }

  private fun setCurrentTime() {
    _state.update {
      it.copy(
        currentDay = dateTimeRepository.currentTimeDay,
        currentMonth = dateTimeRepository.currentTimeMonth,
        currentYear = dateTimeRepository.currentTimeYear,
      )
    }
  }

  private fun getRoutines(yearNumber: Int = lastYearNumber, monthNumber: Int = lastMonthNumber, numberDay: Int = lastDayNumber, searchText: String = "") {
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
    _state.update {
      it.copy(routines = LoadableData.Loading)
    }
    getRemindersUseCase.invoke(monthNumber, numberDay, yearNumber, scope).collectLatest { routines ->
      _state.update {
        it.copy(
          routines = LoadableData.Loaded(
            routines.map { it.toRoutineUiModel() }.sortedBy {
              it.timeHours?.replace(":", "")?.toInt()
            }.toPersistentList(),
          ),
        )
      }
    }
  }

  private suspend fun searchRoutine(searchText: String, scope: CoroutineScope, yearNumber: Int, monthNumber: Int, numberDay: Int) {
    _state.update {
      it.copy(routines = LoadableData.Loading)
    }
    searchRoutineUseCase.invoke(searchText, yearNumber, monthNumber, numberDay, scope).collectLatest { searchItems ->
      Timber.tag("routineSearch").d("searchRoutine->$searchText")
      Timber.tag("routineSearch").d("searchItems->$searchItems")
      _state.update {
        it.copy(
          routines = LoadableData.Loaded(
            searchItems.map { it.toRoutineUiModel() }.sortedBy {
              it.timeHours?.replace(":", "")?.toInt()
            }.toPersistentList(),
          ),
        )
      }
    }
  }

  private fun deleteRoutine(routine: RoutineUiModel) {
    viewModelScope.launch {
      deleteReminderUseCase(routine = routine.toRoutine())
    }
  }

  private fun updateRoutine(routine: RoutineUiModel) {
    viewModelScope.launch {
      Timber.tag("routineViewModel").d("GetRoutines")
      when (val response = updateReminderUseCase(routine = routine.toRoutine())) {
        is Resource.Error -> {
          _state.update { state ->
            state.copy(
              errorMessageCode = response.error,
            )
          }
        }

        is Resource.Success -> {}
      }
    }
  }

  private fun checkedRoutine(routine: RoutineUiModel) {
    viewModelScope.launch {
      cancelReminderUseCase(routine = routine.toRoutine())
    }
  }

  private fun addRoutine(routine: RoutineUiModel) {
    viewModelScope.launch {
      when (val response = addReminderUseCase(routine.toRoutine())) {
        is Resource.Error -> {
          _state.update { state ->
            state.copy(
              errorMessageCode = response.error,
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
        _state.update {
          it.copy(
            times = times.map { it.toTimeDateUiModel() }.toPersistentList(),
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
      _state.update {
        it.copy(timesMonth = times.map { it.toTimeDateUiModel() }.toPersistentList())
      }
    }
  }

  private fun calculateIndexDay(index: Int) = index.minus(index % 7)

  private fun updateDayChecked(yearNumber: Int, monthNumber: Int, day: Int = DAY_MIN) {
    viewModelScope.launch {
      dateTimeRepository.updateDayToToday(day, yearNumber, monthNumber)
      _state.update {
        it.copy(currentMonth = monthNumber, currentYear = yearNumber, currentDay = day)
      }
    }
  }
}
