package com.rahim.ui.home

import androidx.lifecycle.viewModelScope
import com.rahim.data.modle.Rotin.Routine
import com.rahim.data.repository.base.BaseRepository
import com.rahim.data.repository.home.HomeRepository
import com.rahim.data.repository.routine.RepositoryRoutine
import com.rahim.data.repository.sharedPreferences.SharedPreferencesRepository
import com.rahim.utils.base.viewModel.BaseViewModel
import com.rahim.utils.resours.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
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
    private val _flowRoutines =
        MutableStateFlow<Resource<List<Routine>>>(Resource.Success(emptyList()))
    val flowRoutines = _flowRoutines.asStateFlow()

    private val _addRoutine =
        MutableStateFlow<Resource<Routine?>?>(null)
    val addRoutine = _addRoutine.asStateFlow()

    private val _updateRoutine =
        MutableStateFlow<Resource<Routine?>?>(null)
    val updateRoutine = _updateRoutine.asStateFlow()
    init {
        getCurrentRoutines()
    }
    private fun getCurrentRoutines() {
        viewModelScope.launch {
            _flowRoutines.value = Resource.Loading()
            routineRepository.getCurrentRoutines().catch {
                _flowRoutines.value = Resource.Error(errorGetProses)
            }.collect {
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
                _addRoutine.value=Resource.Error(errorSaveProses)
            }.collect {
                Timber.tag("routineAdd")
                    .d("view model ->${if (it is Resource.Success) "success" else if (it is Resource.Error) "fail" else "loading"}")
                _addRoutine.value = it
            }
        }
    }
    fun clearAddRoutine(){
        _addRoutine.value=null
    }
    fun clearUpdateRoutine(){
        _updateRoutine.value=null
    }
}
