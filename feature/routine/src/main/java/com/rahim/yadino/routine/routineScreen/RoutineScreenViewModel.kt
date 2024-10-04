package com.rahim.yadino.routine.routineScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahim.yadino.Resource
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
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
  ViewModel() {

  private var lastYearNumber = dateTimeRepository.currentTimeYer
  private var lastMonthNumber = dateTimeRepository.currentTimeMonth
  private var lastDayNumber = dateTimeRepository.currentTimeDay
  val currentYear = dateTimeRepository.currentTimeYer
  val currentMonth = dateTimeRepository.currentTimeMonth
  val currentDay = dateTimeRepository.currentTimeDay

  private val _flowRoutines =
    MutableStateFlow<Resource<List<RoutineModel>>>(Resource.Loading())
  val flowRoutines: StateFlow<Resource<List<RoutineModel>>> = _flowRoutines.stateIn(viewModelScope, started = SharingStarted.WhileSubscribed(5_000), initialValue = Resource.Loading())

  private val _times =
    MutableStateFlow<List<TimeDate>>(emptyList())
  val times: StateFlow<List<TimeDate>> = _times

  private val _addRoutineModel =
    MutableStateFlow<Resource<Nothing?>?>(null)
  val addRoutineModel: StateFlow<Resource<Nothing?>?> = _addRoutineModel
  private val _updateRoutineModel =
    MutableStateFlow<Resource<Nothing?>?>(null)
  val updateRoutineModel: StateFlow<Resource<Nothing?>?> = _updateRoutineModel

  private val _indexDay =
    MutableStateFlow(0)
  val indexDay: StateFlow<Int> = _indexDay

  init {
    getRoutines()
    calculateIndexDay()
    getTimesMonth()
  }

  fun getRoutines(
    yerNumber: Int = dateTimeRepository.currentTimeYer,
    monthNumber: Int = dateTimeRepository.currentTimeMonth,
    numberDay: Int = dateTimeRepository.currentTimeDay,
  ) {
    viewModelScope.launch {
      lastYearNumber = yerNumber
      lastMonthNumber = monthNumber
      lastDayNumber = numberDay
      Timber.tag("routineGetNameDay").d("getRoutines model monthNumber->$monthNumber")
      Timber.tag("routineGetNameDay").d("getRoutines model numberDay->$numberDay")
      Timber.tag("routineGetNameDay").d("getRoutines model yerNumber->$yerNumber")
      _flowRoutines.value = Resource.Success(
        getRemindersUseCase(lastMonthNumber, lastDayNumber, lastYearNumber).sortedBy {
          it.timeHours?.replace(":", "")?.toInt()
        },
      )
    }
  }

  fun deleteRoutine(routineModel: RoutineModel) {
    viewModelScope.launch {
      deleteReminderUseCase(routineModel)
      getRoutines(lastYearNumber, lastMonthNumber, lastDayNumber)
    }
  }

  fun updateRoutine(routineModel: RoutineModel) {
    viewModelScope.launch {
      updateReminderUseCase(routineModel).catch {}.collectLatest {
        _updateRoutineModel.value = it
        getRoutines(lastYearNumber, lastMonthNumber, lastDayNumber)
      }
    }
  }

  fun checkedRoutine(routineModel: RoutineModel) {
    viewModelScope.launch {
      cancelReminderUseCase(routineModel)
      getRoutines(lastYearNumber, lastMonthNumber, lastDayNumber)
    }
  }

  fun addRoutine(routineModel: RoutineModel) {
    viewModelScope.launch {
      addReminderUseCase(routineModel).catch {}.collectLatest {
        _addRoutineModel.value = it
        getRoutines(lastYearNumber, lastMonthNumber, lastDayNumber)
      }
    }
  }

  fun getTimes(): Flow<List<TimeDate>> = flow {
    emitAll(dateTimeRepository.getTimes())
  }

  fun getTimesMonth(yearNumber: Int = dateTimeRepository.currentTimeYer, monthNumber: Int = dateTimeRepository.currentTimeMonth) {
    viewModelScope.launch {
      dateTimeRepository.getTimesMonth(yearNumber, monthNumber).catch {}.collectLatest {
        _times.value = it
      }
    }
  }

  fun clearAddRoutine() {
    _addRoutineModel.value = null
  }

  fun clearUpdateRoutine() {
    _updateRoutineModel.value = null
  }

  private fun calculateIndexDay() {
    viewModelScope.launch {
      var timesSize = 0
      dateTimeRepository.getTimes().distinctUntilChanged().catch {}.collectLatest { times ->
        if (timesSize != times.size) {
          timesSize = times.size
          val currentTime = times.find { it.isToday }
          val indexCurrentDay = times.indexOf(currentTime)
          _indexDay.value = calculateIndexDay(indexCurrentDay)
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

  fun setDayIndex(index: Int) {
    _indexDay.value = index
  }

  fun searchItems(searchText: String) {
    viewModelScope.launch {
      if (searchText.isNotEmpty()) {
        _flowRoutines.value= Resource.Loading()
        Timber.tag("searchRoutine").d("searchText:$searchText")
        val searchItems = searchRoutineUseCase(searchText, lastYearNumber, lastMonthNumber, lastDayNumber)
        _flowRoutines.value= Resource.Success(
          searchItems.sortedBy {
            it.timeHours?.replace(":", "")?.toInt()
          },
        )
      } else {
        getRoutines()
      }
    }
  }

  fun showSampleRoutine(isShow: Boolean = true) {
    viewModelScope.launch {
      sharedPreferencesRepository.isShowSampleRoutine(isShow)
    }
  }
}
