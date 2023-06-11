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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
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
        MutableStateFlow<Resource<List<Routine>>>(Resource.Success(emptyList()))
    val flowRoutines: StateFlow<Resource<List<Routine>>> = _flowRoutines

    private val _flowNameDay =
        MutableStateFlow("")
    val flowNameDay: StateFlow<String> = _flowNameDay

    init {
        getRoutines(currentMonth, currentDay, currentYer)
    }

    fun getCurrentNameDay(date: String, format: String) {
        viewModelScope.launch {
            val time = routineRepository.getCurrentNameDay(date, format)
            _flowNameDay.value = time
        }
    }

    fun getRoutines(
        monthNumber: Int, numberDay: Int, yerNumber: Int
    ) {
        viewModelScope.launch {
            _flowRoutines.value = Resource.Loading()
            routineRepository.getRoutine(monthNumber, numberDay, yerNumber).catch {
                _flowRoutines.value = Resource.Error(errorGetProses)
            }.collect {
                _flowRoutines.value = Resource.Success(it)
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

    fun addRoutine(routine: Routine) {
        viewModelScope.launch {
            routineRepository.addRoutine(routine)
        }
    }

    fun getCurrentMonthDay(monthNumber: Int, yerNumber: Int?): Flow<List<TimeData>> = flow {
        emitAll(dateTimeRepository.getCurrentMonthDay(monthNumber, yerNumber))
    }
}