package com.rahim.yadino.routine.homeScreen

import android.R.id.message
import androidx.lifecycle.viewModelScope
import com.rahim.yadino.Resource
import com.rahim.yadino.base.BaseViewModel
import com.rahim.yadino.enums.error.ErrorMessageCode
import com.rahim.yadino.routine.useCase.AddReminderUseCase
import com.rahim.yadino.model.RoutineModel
import com.rahim.yadino.dateTime.DateTimeRepository
import com.rahim.yadino.routine.useCase.CancelReminderUseCase
import com.rahim.yadino.routine.useCase.DeleteReminderUseCase
import com.rahim.yadino.routine.useCase.GetRemindersUseCase
import com.rahim.yadino.routine.useCase.SearchRoutineUseCase
import com.rahim.yadino.routine.useCase.UpdateReminderUseCase
import com.rahim.yadino.sharedPreferences.SharedPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
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
) : BaseViewModel(), HomeContract {
  val currentYear = dateTimeRepository.currentTimeYer
  val currentMonth = dateTimeRepository.currentTimeMonth
  val currentDay = dateTimeRepository.currentTimeDay

  private val mutableState = MutableStateFlow(HomeContract.HomeState())
  override val state: StateFlow<HomeContract.HomeState> = mutableState.onStart {
    getCurrentRoutines()
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HomeContract.HomeState())

  override fun event(event: HomeContract.HomeEvent) {
    when (event) {
      HomeContract.HomeEvent.GetRoutines -> {
        getCurrentRoutines()
      }

      is HomeContract.HomeEvent.AddRoutine -> {
        addRoutine(event.routine)
      }

      is HomeContract.HomeEvent.UpdateRoutine -> {
        updateRoutine(event.routine)
      }

      is HomeContract.HomeEvent.CheckedRoutine -> {
        checkedRoutine(event.routine)
      }

      is HomeContract.HomeEvent.DeleteRoutine -> {
        deleteRoutine(event.routine)
      }

      is HomeContract.HomeEvent.SearchRoutine -> {
        searchItems(event.routineName)
      }

      HomeContract.HomeEvent.ShowSampleRoutines -> {
        showSampleRoutine()
      }
    }
  }

  private fun getCurrentRoutines() {
    viewModelScope.launch {
      getRemindersUseCase(dateTimeRepository.currentTimeMonth, dateTimeRepository.currentTimeDay, dateTimeRepository.currentTimeYer).catch {

      }.collect { routines ->
        mutableState.update {
          it.copy(
            routines = routines.sortedBy {
              it.timeHours?.replace(":", "")?.toInt()
            },
            errorMessage = null
          )
        }
      }
    }
  }

  private fun deleteRoutine(routineModel: RoutineModel) {
    viewModelScope.launch {
      deleteReminderUseCase(routineModel)
    }
  }

  private fun updateRoutine(routineModel: RoutineModel) {
    Timber.tag("addRoutine").d("updateRoutine")
    viewModelScope.launch {
      val response = updateReminderUseCase(routineModel)
      when (response) {
        is Resource.Error -> {
          mutableState.update { state ->
            state.copy(
              errorMessage = response.message,
            )
          }
        }

        is Resource.Success -> {}
      }
    }
  }

  private fun checkedRoutine(routineModel: RoutineModel) {
    viewModelScope.launch {
      cancelReminderUseCase(routineModel)
    }
  }

  private fun addRoutine(routineModel: RoutineModel) {
    viewModelScope.launch {
      Timber.tag("addRoutine").d("addRoutine")
      val response = addReminderUseCase(routineModel)
      when (response) {
        is Resource.Error -> {
          mutableState.update { state ->
            state.copy(
              errorMessage = response.message,
            )
          }
        }

        is Resource.Success -> {}
      }
    }
  }

  private fun searchItems(searchText: String) {
    viewModelScope.launch {
      if (searchText.isNotEmpty()) {
        Timber.tag("searchRoutine").d("searchText:$searchText")
        val searchItems = searchRoutineUseCase(searchText, dateTimeRepository.currentTimeYer, dateTimeRepository.currentTimeMonth, dateTimeRepository.currentTimeDay)
        mutableState.update {
          it.copy(
            routines = searchItems.sortedBy {
              it.timeHours?.replace(":", "")?.toInt()
            },
            errorMessage = null
          )
        }
      } else {
        getCurrentRoutines()
      }
    }
  }

  private fun showSampleRoutine(isShow: Boolean = true) {
    viewModelScope.launch {
      sharedPreferencesRepository.isShowSampleRoutine(isShow)
    }
  }

}
