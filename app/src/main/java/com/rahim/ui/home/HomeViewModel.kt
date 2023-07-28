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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
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
    val addRoutine = MutableStateFlow(0L)
    fun getCurrentRoutines(): Flow<Resource<List<Routine>>> = flow {
        emit(Resource.Loading())
        routineRepository.getCurrentRoutines().catch {
            emit(Resource.Error(errorGetProses))
        }.collect {
            emit(Resource.Success(it))
        }
    }

    fun deleteRoutine(routine: Routine){
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
            val id = async {
                routineRepository.addRoutine(routine)
            }.await()
            addRoutine.value = id
        }
    }
}
