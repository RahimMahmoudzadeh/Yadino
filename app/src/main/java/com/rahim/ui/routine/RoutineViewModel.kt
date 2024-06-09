package com.rahim.ui.routine

import androidx.lifecycle.viewModelScope
import com.rahim.data.modle.Rotin.Routine
import com.rahim.data.modle.data.TimeDate
import com.rahim.data.modle.data.TimeDataMonthAndYear
import com.rahim.data.repository.base.BaseRepository
import com.rahim.data.repository.dataTime.DataTimeRepository
import com.rahim.data.repository.routine.RepositoryRoutine
import com.rahim.data.repository.sharedPreferences.SharedPreferencesRepository
import com.rahim.utils.base.viewModel.BaseViewModel
import com.rahim.utils.enums.error.ErrorMessageCode
import com.rahim.utils.resours.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RoutineViewModel @Inject constructor(
    private val routineRepository: RepositoryRoutine,
    private val dateTimeRepository: DataTimeRepository,
    baseRepository: BaseRepository,
    sharedPreferencesRepository: SharedPreferencesRepository
) :
    BaseViewModel(sharedPreferencesRepository, baseRepository) {

    private val _flowRoutines =
        MutableStateFlow<Resource<List<Routine>>>(Resource.Loading())
    val flowRoutines: StateFlow<Resource<List<Routine>>> = _flowRoutines.asStateFlow()

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
        getRoutines(currentMonth, currentDay, currentYer)
        calculateIndexDay()
    }

    fun getRoutines(
        yerNumber: Int = currentYer, monthNumber: Int = currentMonth, numberDay: Int = currentDay,
    ) {
        viewModelScope.launch {
            _flowRoutines.value = Resource.Loading()
            Timber.tag("routineGetNameDay").d("getRoutines model monthNumber->$monthNumber")
            Timber.tag("routineGetNameDay").d("getRoutines model numberDay->$numberDay")
            Timber.tag("routineGetNameDay").d("getRoutines model yerNumber->$yerNumber")
            routineRepository.getRoutines(monthNumber, numberDay, yerNumber).catch {
                _flowRoutines.value = Resource.Error(ErrorMessageCode.ERROR_GET_PROCESS)
            }.collectLatest {
                Timber.tag("routineGetNameDay").d("getRoutines routines->$it")
                _flowRoutines.value = Resource.Success(it.sortedBy {
                    it.timeHours?.replace(":", "")?.toInt()
                })
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

    fun getTimesMonth(yerNumber: Int, monthNumber: Int): Flow<List<TimeDate>> = flow {
        emitAll(dateTimeRepository.getTimesMonth(yerNumber, monthNumber))
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
                Timber.tag("searchRoutine").d("searchText:$searchText")
                routineRepository.searchRoutine(searchText, currentMonth, currentDay).catch {
                    _flowRoutines.value = Resource.Error(ErrorMessageCode.ERROR_GET_PROCESS)
                }.collect {
                    _flowRoutines.value = it
                }
            } else {
                getRoutines()
            }
        }
    }
}