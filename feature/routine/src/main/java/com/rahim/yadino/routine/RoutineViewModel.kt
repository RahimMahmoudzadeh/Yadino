package com.rahim.yadino.routine

import androidx.lifecycle.viewModelScope
import com.rahim.yadino.base.Resource
import com.rahim.yadino.base.enums.error.ErrorMessageCode
import com.rahim.yadino.base.model.TimeDate
import com.rahim.yadino.base.viewmodel.BaseViewModel
import com.rahim.yadino.dateTime.DataTimeRepository
import com.rahim.yadino.routine.modle.Routine.Routine
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
    private val dateTimeRepository: DataTimeRepository,
    private val sharedPreferencesRepository: SharedPreferencesRepository,
) :
    BaseViewModel() {

    private var lastYearNumber = currentYear
    private var lastMonthNumber = currentMonth
    private var lastDayNumber = currentDay

    private val _flowRoutines =
        MutableStateFlow<Resource<List<Routine>>>(Resource.Loading())
    val flowRoutines: StateFlow<Resource<List<Routine>>> = _flowRoutines

    private val _times =
        MutableStateFlow<List<TimeDate>>(emptyList())
    val times: StateFlow<List<TimeDate>> = _times

    private val _addRoutine =
        MutableStateFlow<Resource<Routine?>?>(null)
    val addRoutine: StateFlow<Resource<Routine?>?> = _addRoutine
    private val _updateRoutine =
        MutableStateFlow<Resource<Routine?>?>(null)
    val updateRoutine: StateFlow<Resource<Routine?>?> = _updateRoutine

    private val _indexDay =
        MutableStateFlow(0)
    val indexDay: StateFlow<Int> = _indexDay

    init {
        getRoutines()
        calculateIndexDay()
        getTimesMonth()
    }

    fun getRoutines(
        yerNumber: Int = currentYear,
        monthNumber: Int = currentMonth,
        numberDay: Int = currentDay,
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
                                })
                } else {
                    _flowRoutines.value = Resource.Success(emptyList())
                }
            }
        }
    }

    fun deleteRoutine(routine: Routine) {
        viewModelScope.launch {
            routineRepository.removeRoutine(routine)
        }
    }

    fun updateRoutine(routine: Routine) {
        viewModelScope.launch {
            routineRepository.updateRoutine(routine).catch {}.collectLatest {
                _updateRoutine.value = it
            }
        }
    }

    fun checkedRoutine(routine: Routine) {
        viewModelScope.launch {
            routineRepository.checkedRoutine(routine)
        }
    }

    fun addRoutine(routine: Routine) {
        viewModelScope.launch {
            routineRepository.addRoutine(routine).catch {}.collectLatest {
                _addRoutine.value = it
            }
        }
    }

    fun getTimes(): Flow<List<TimeDate>> = flow {
        emitAll(dateTimeRepository.getTimes())
    }

    fun getTimesMonth(yearNumber: Int = currentYear, monthNumber: Int = currentMonth) {
        viewModelScope.launch {
            dateTimeRepository.getTimesMonth(yearNumber, monthNumber).catch {}.collectLatest {
                _times.value = it
            }
        }
    }

    fun clearAddRoutine() {
        _addRoutine.value = null
    }

    fun clearUpdateRoutine() {
        _updateRoutine.value = null
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
                routineRepository.searchRoutine(searchText, currentMonth, currentDay).catch {
                    _flowRoutines.value = Resource.Error(ErrorMessageCode.ERROR_GET_PROCESS)
                }.collectLatest {
                    if (it.isNotEmpty()) {
                        val firstRoutine = it.first()
                        if (firstRoutine.dayNumber == lastDayNumber && firstRoutine.yerNumber == lastYearNumber && firstRoutine.monthNumber == lastMonthNumber)
                            _flowRoutines.value =
                                Resource.Success(
                                    it.sortedBy {
                                        it.timeHours?.replace(":", "")?.toInt()
                                    })
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