package com.rahim.ui.home

import androidx.lifecycle.viewModelScope
import com.rahim.data.modle.Rotin.Routine
import com.rahim.data.repository.home.HomeRepository
import com.rahim.data.repository.sharedPreferences.SharedPreferencesRepository
import com.rahim.utils.base.viewModel.BaseViewModel
import com.rahim.utils.resours.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    sharedPreferencesRepository: SharedPreferencesRepository
) :
    BaseViewModel(sharedPreferencesRepository) {
    fun getCurrentTime(): List<Int> = homeRepository.getCurrentTime()

    fun getCurrentRoutines(): Flow<Resource<List<Routine>>> = flow {
        emit(Resource.Loading())
        homeRepository.getCurrentRoutines().catch {
            emit(Resource.Error(errorGetProses))
        }.collect {
            emit(Resource.Success(it))
        }
    }

    fun deleteRoutine(routine: Routine) {
        viewModelScope.launch {
            homeRepository.deleteRoutine(routine)
        }
    }

    fun updateRoutine(routine: Routine) {
        viewModelScope.launch {
            homeRepository.updateRoutine(routine)
        }
    }

    fun addRoutine(routine: Routine){
        viewModelScope.launch {
            homeRepository.addRoutine(routine)
        }
    }
}
