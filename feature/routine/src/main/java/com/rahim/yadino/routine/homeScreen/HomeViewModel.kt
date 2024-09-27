package com.rahim.yadino.routine.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahim.yadino.routine.RepositoryRoutine
import com.rahim.yadino.base.Resource
import com.rahim.yadino.base.enums.error.ErrorMessageCode
import com.rahim.yadino.routine.useCase.AddReminderUseCase
import com.rahim.yadino.base.model.RoutineModel
import com.rahim.yadino.dateTime.DateTimeRepository
import com.rahim.yadino.routine.useCase.CancelReminderUseCase
import com.rahim.yadino.routine.useCase.DeleteReminderUseCase
import com.rahim.yadino.routine.useCase.GetRemindersUseCase
import com.rahim.yadino.routine.useCase.SearchRoutineUseCase
import com.rahim.yadino.routine.useCase.UpdateReminderUseCase
import com.rahim.yadino.sharedPreferences.SharedPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val addReminderUseCase: AddReminderUseCase,
  private val updateReminderUseCase: UpdateReminderUseCase,
  private val cancelReminderUseCase: CancelReminderUseCase,
  private val deleteReminderUseCase: DeleteReminderUseCase,
  private val getRemindersUseCase: GetRemindersUseCase,
  private val searchRoutineUseCase: SearchRoutineUseCase,
  private val dateTimeRepository: DateTimeRepository,
  private val sharedPreferencesRepository: SharedPreferencesRepository,
) :
  ViewModel() {
  val currentYear = dateTimeRepository.currentTimeYer
  val currentMonth = dateTimeRepository.currentTimeMonth
  val currentDay = dateTimeRepository.currentTimeDay

  private var _flowRoutines = MutableStateFlow<Resource<List<RoutineModel>>>(Resource.Loading())
  val flowRoutines: StateFlow<Resource<List<RoutineModel>>> = _flowRoutines.onStart {
    getCurrentRoutines()
  }.stateIn(viewModelScope, started = SharingStarted.WhileSubscribed(5_000), initialValue = Resource.Loading())

  private val _addRoutine =
    MutableStateFlow<Resource<Nothing?>?>(null)
  val addRoutine = _addRoutine

  private val _updateRoutineModel =
    MutableStateFlow<Resource<Nothing?>?>(null)
  val updateRoutine = _updateRoutineModel

  private fun getCurrentRoutines() {
    viewModelScope.launch {
      _flowRoutines.value = Resource.Success(
        getRemindersUseCase(dateTimeRepository.currentTimeMonth, dateTimeRepository.currentTimeDay, dateTimeRepository.currentTimeYer).sortedBy {
          it.timeHours?.replace(":", "")?.toInt()
        },
      )
    }
  }

  fun deleteRoutine(routineModel: RoutineModel) {
    viewModelScope.launch {
      deleteReminderUseCase(routineModel)
      getCurrentRoutines()
    }
  }

  fun updateRoutine(routineModel: RoutineModel) {
    Timber.tag("addRoutine").d("updateRoutine")
    viewModelScope.launch {
      updateReminderUseCase(routineModel).catch {
        _updateRoutineModel.value = Resource.Error(ErrorMessageCode.ERROR_SAVE_PROSES)
      }.collect {
        Timber.tag("addRoutine").d("updateRoutine->$it")
        Timber.tag("routineAdd")
          .d("view model ->${if (it is Resource.Success) "success" else if (it is Resource.Error) "fail" else "loading"}")
        _updateRoutineModel.value = it
        getCurrentRoutines()
      }
    }
  }

  fun checkedRoutine(routineModel: RoutineModel) {
    viewModelScope.launch {
      cancelReminderUseCase(routineModel)
      getCurrentRoutines()
    }
  }

  fun addRoutine(routineModel: RoutineModel) {
    viewModelScope.launch {
      Timber.tag("addRoutine").d("addRoutine")
      addReminderUseCase(routineModel).catch {
        _addRoutine.value = Resource.Error(ErrorMessageCode.ERROR_SAVE_PROSES)
      }.collect {
        Timber.tag("addRoutine").d("addRoutine-> $it")
        Timber.tag("routineAdd")
          .d("view model ->${if (it is Resource.Success) "success" else if (it is Resource.Error) "fail" else "loading"}")
        _addRoutine.value = it
        getCurrentRoutines()
      }
    }
  }

  fun clearAddRoutine() {
    _addRoutine.value = null
  }

  fun clearUpdateRoutine() {
    _updateRoutineModel.value = null
  }

  fun searchItems(searchText: String) {
    viewModelScope.launch {
      if (searchText.isNotEmpty()) {
        _flowRoutines.value = Resource.Loading()
        Timber.tag("searchRoutine").d("searchText:$searchText")
        val searchItems = searchRoutineUseCase(searchText, dateTimeRepository.currentTimeYer, dateTimeRepository.currentTimeMonth, dateTimeRepository.currentTimeDay)
        _flowRoutines.value = Resource.Success(
          searchItems.sortedBy {
            it.timeHours?.replace(":", "")?.toInt()
          },
        )
      } else {
        getCurrentRoutines()
      }
    }
  }

  fun showSampleRoutine(isShow: Boolean = true) {
    viewModelScope.launch {
      sharedPreferencesRepository.isShowSampleRoutine(isShow)
    }
  }

}
