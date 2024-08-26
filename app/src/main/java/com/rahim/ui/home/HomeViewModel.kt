package com.rahim.ui.home

import androidx.lifecycle.viewModelScope
import com.rahim.data.model.routine.RoutineModel
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
  sharedPreferencesRepository: SharedPreferencesRepository,
) :
  BaseViewModel(sharedPreferencesRepository, baseRepository) {

  private var _flowRoutines = MutableStateFlow<Resource<List<RoutineModel>>>(Resource.Loading())
  val flowRoutines: StateFlow<Resource<List<RoutineModel>>> = _flowRoutines

  private val _addRoutineModel =
    MutableStateFlow<Resource<RoutineModel?>?>(null)
  val addRoutine = _addRoutineModel

  private val _updateRoutineModel =
    MutableStateFlow<Resource<RoutineModel?>?>(null)
  val updateRoutine = _updateRoutineModel

  init {
    getCurrentRoutines()
  }

  private fun getCurrentRoutines() {
    viewModelScope.launch {
      routineRepository.getRoutines(currentMonth, currentDay, currentYear)
        .catch { _flowRoutines.value = Resource.Error(ErrorMessageCode.ERROR_GET_PROCESS) }
        .collect {
          _flowRoutines.value = Resource.Success(
            it.sortedBy {
              it.timeHours?.replace(":", "")?.toInt()
            },
          )
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
      routineRepository.addRoutine(routineModel).catch {
        _addRoutineModel.value = Resource.Error(ErrorMessageCode.ERROR_SAVE_PROSES)
      }.collect {
        Timber.tag("routineAdd")
          .d("view model ->${if (it is Resource.Success) "success" else if (it is Resource.Error) "fail" else "loading"}")
        _addRoutineModel.value = it
      }
    }
  }

  fun clearAddRoutine() {
    _addRoutineModel.value = null
  }

  fun clearUpdateRoutine() {
    _updateRoutineModel.value = null
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
            if (firstRoutine.dayNumber == currentDay && firstRoutine.yerNumber == currentYear && firstRoutine.monthNumber == currentMonth) {
              _flowRoutines.value =
                Resource.Success(
                  it.sortedBy {
                    it.timeHours?.replace(":", "")?.toInt()
                  },
                )
            }
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
