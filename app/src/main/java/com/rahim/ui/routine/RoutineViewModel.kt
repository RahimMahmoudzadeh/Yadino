package com.rahim.ui.routine

import androidx.lifecycle.viewModelScope
import com.rahim.data.modle.Rotin.Routine
import com.rahim.data.modle.data.TimeData
import com.rahim.data.repository.base.BaseRepository
import com.rahim.data.repository.dataTime.DataTimeRepository
import com.rahim.data.repository.routine.RepositoryRoutine
import com.rahim.data.repository.sharedPreferences.SharedPreferencesRepository
import com.rahim.utils.base.viewModel.BaseViewModel
import com.rahim.utils.resours.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
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

    private var monthNumber: Int? = null
    private var yerNumber: Int? = null
    private var dayNumber: Int? = null

    private val _flowRoutines =
        MutableStateFlow<Resource<List<Routine>>>(Resource.Success(emptyList()))
    val flowRoutines: StateFlow<Resource<List<Routine>>> = _flowRoutines

    private val _addRoutine =
        MutableStateFlow<Resource<Long>>(Resource.Success(0L))
    val addRoutine: StateFlow<Resource<Long>> = _addRoutine

    init {
        getRoutines(currentMonth, currentDay, currentYer)
    }


    fun getRoutines(
        monthNumber: Int, numberDay: Int, yerNumber: Int
    ) {
        this.monthNumber = monthNumber
        this.yerNumber = yerNumber
        this.dayNumber = numberDay
        viewModelScope.launch {
            _flowRoutines.value = Resource.Loading()
            Timber.tag("routineGetNameDay").d("getRoutines model monthNumber->$monthNumber")
            Timber.tag("routineGetNameDay").d("getRoutines model numberDay->$numberDay")
            Timber.tag("routineGetNameDay").d("getRoutines model yerNumber->$yerNumber")
            routineRepository.getRoutines(monthNumber, numberDay, yerNumber).catch {
                _flowRoutines.value = Resource.Error(errorGetProses)
            }.collect {
                if (it.isEmpty())
                    _flowRoutines.value = Resource.Success(it.sortedBy {
                        it.timeHours?.replace(":", "")?.toInt()
                    })
                else {
                    val firstItem = it.first()
                    if (firstItem.dayNumber == this@RoutineViewModel.dayNumber && firstItem.yerNumber == this@RoutineViewModel.yerNumber && firstItem.monthNumber == this@RoutineViewModel.monthNumber) {
                        _flowRoutines.value = Resource.Success(it.sortedBy {
                            it.timeHours?.replace(":", "")?.toInt()
                        })
                    }
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
            routineRepository.updateRoutine(routine)
        }
    }

    fun updateCheckedByAlarmId(id: Long) {
        viewModelScope.launch {
            routineRepository.updateCheckedByAlarmId(id)
        }
    }

    fun addRoutine(routine: Routine) {
        viewModelScope.launch {
            routineRepository.addRoutine(routine).catch {}.collect {
                _addRoutine.value = it
            }
        }
    }

    fun getCurrentMonthDay(monthNumber: Int, yerNumber: Int?): Flow<List<TimeData>> = flow {
        emitAll(dateTimeRepository.getCurrentMonthDay(monthNumber, yerNumber))
    }
}