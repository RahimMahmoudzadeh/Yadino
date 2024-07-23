package com.rahim.yadino.home

import androidx.lifecycle.viewModelScope
import com.rahim.data.modle.Rotin.Routine
import com.rahim.data.repository.base.BaseRepository
import com.rahim.data.repository.routine.RepositoryRoutine
import com.rahim.data.repository.sharedPreferences.SharedPreferencesRepository
import com.rahim.utils.base.viewModel.BaseViewModel
import com.rahim.utils.enums.error.ErrorMessageCode
import com.rahim.utils.resours.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val routineRepository: RepositoryRoutine,
    baseRepository: BaseRepository,
    sharedPreferencesRepository: SharedPreferencesRepository
) :
    BaseViewModel(sharedPreferencesRepository, baseRepository) {

    private var _flowRoutines = MutableStateFlow<Resource<List<Routine>>>(Resource.Loading())
    val flowRoutines: StateFlow<Resource<List<Routine>>> = _flowRoutines

    private val _addRoutine =
        MutableStateFlow<Resource<Routine?>?>(null)
    val addRoutine = _addRoutine

    private val _updateRoutine =
        MutableStateFlow<Resource<Routine?>?>(null)
    val updateRoutine = _updateRoutine


    init {
        getCurrentRoutines()
    }

    private fun getCurrentRoutines() {
        viewModelScope.launch {
            routineRepository.getRoutines(currentMonth, currentDay, currentYear)
                .catch { _flowRoutines.value = Resource.Error(ErrorMessageCode.ERROR_GET_PROCESS) }
                .collect {
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
            routineRepository.addRoutine(routine).catch {
                _addRoutine.value = Resource.Error(ErrorMessageCode.ERROR_SAVE_PROSES)
            }.collect {
                Timber.tag("routineAdd")
                    .d("view model ->${if (it is Resource.Success) "success" else if (it is Resource.Error) "fail" else "loading"}")
                _addRoutine.value = it
            }
        }
    }

    fun clearAddRoutine() {
        _addRoutine.value = null
    }

    fun clearUpdateRoutine() {
        _updateRoutine.value = null
    }

    fun searchItems(searchText: String) {
        viewModelScope.launch {
            if (searchText.isNotEmpty()) {
                _flowRoutines.value = Resource.Loading()
                Timber.tag("searchRoutine").d("searchText:$searchText")
                routineRepository.searchRoutine(searchText, currentMonth, currentDay).catch {
                    _flowRoutines.value = Resource.Error(ErrorMessageCode.ERROR_GET_PROCESS)
                }.collect {
                    if (it.isNotEmpty()) {
                        val firstRoutine = it.first()
                        if (firstRoutine.dayNumber == currentDay && firstRoutine.yerNumber == currentYear && firstRoutine.monthNumber == currentMonth)
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
                getCurrentRoutines()
            }
        }
    }


}
