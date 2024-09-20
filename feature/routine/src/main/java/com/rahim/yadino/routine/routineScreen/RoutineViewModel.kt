package com.rahim.yadino.routine.routineScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahim.yadino.base.Resource
import com.rahim.yadino.base.enums.error.ErrorMessageCode
import com.rahim.yadino.base.db.model.TimeDate
import com.rahim.yadino.dateTime.DateTimeRepository
import com.rahim.yadino.routine.RepositoryRoutine
import com.rahim.yadino.base.db.model.RoutineModel
import com.rahim.yadino.sharedPreferences.SharedPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RoutineViewModel @Inject constructor(
  private val routineRepository: RepositoryRoutine,
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
  val flowRoutines: StateFlow<Resource<List<RoutineModel>>> = _flowRoutines

  private val _times =
    MutableStateFlow<List<TimeDate>>(emptyList())
  val times: StateFlow<List<TimeDate>> = _times

  private val _addRoutineModel =
    MutableStateFlow<Resource<RoutineModel?>?>(null)
  val addRoutineModel: StateFlow<Resource<RoutineModel?>?> = _addRoutineModel
  private val _updateRoutineModel =
    MutableStateFlow<Resource<RoutineModel?>?>(null)
  val updateRoutineModel: StateFlow<Resource<RoutineModel?>?> = _updateRoutineModel

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
      _flowRoutines.value = Resource.Loading()
      Timber.tag("routineGetNameDay").d("getRoutines model monthNumber->$monthNumber")
      Timber.tag("routineGetNameDay").d("getRoutines model numberDay->$numberDay")
      Timber.tag("routineGetNameDay").d("getRoutines model yerNumber->$yerNumber")
      routineRepository.getRoutines(lastMonthNumber, lastDayNumber, lastYearNumber).catch {
        _flowRoutines.value = Resource.Error(ErrorMessageCode.ERROR_GET_PROCESS)
      }.collectLatest {
        Timber.tag("routineGetNameDay").d("getRoutines routines->$it")
        if (it.isNotEmpty()) {
          _flowRoutines.value =
            Resource.Success(
                it.sortedBy {
                    it.timeHours?.replace(":", "")?.toInt()
                },
            )
        } else {
          _flowRoutines.value = Resource.Success(emptyList())
        }
      }
    }
  }

  fun deleteRoutine(routineModel: RoutineModel) {
    viewModelScope.launch {
      routineRepository.removeRoutine(routineModel)
    }
  }

  fun updateRoutine(routineModel: RoutineModel) {
    viewModelScope.launch {
      routineRepository.updateRoutine(routineModel).catch {}.collectLatest {
        _updateRoutineModel.value = it
      }
    }
  }

  fun checkedRoutine(routineModel: RoutineModel) {
    viewModelScope.launch {
      routineRepository.checkedRoutine(routineModel)
    }
  }

  fun addRoutine(routineModel: RoutineModel) {
    viewModelScope.launch {
      routineRepository.addRoutine(routineModel).catch {}.collectLatest {
        _addRoutineModel.value = it
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
        _flowRoutines.value = Resource.Loading()
        Timber.tag("searchRoutine").d("searchText:$searchText")
        routineRepository.searchRoutine(searchText, dateTimeRepository.currentTimeMonth, dateTimeRepository.currentTimeYer).catch {
          _flowRoutines.value = Resource.Error(ErrorMessageCode.ERROR_GET_PROCESS)
        }.collectLatest {
          if (it.isNotEmpty()) {
            val firstRoutine = it.first()
            if (firstRoutine.dayNumber == lastDayNumber && firstRoutine.yerNumber == lastYearNumber && firstRoutine.monthNumber == lastMonthNumber)
              _flowRoutines.value =
                Resource.Success(
                    it.sortedBy {
                        it.timeHours?.replace(":", "")?.toInt()
                    },
                )
          } else {
            _flowRoutines.value = Resource.Success(emptyList())
          }
        }
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
