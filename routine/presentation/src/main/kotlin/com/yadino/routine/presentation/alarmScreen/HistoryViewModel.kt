package com.yadino.routine.presentation.alarmScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yadino.routine.domain.useCase.GetAllRoutineUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
  private val getRoutineUseCase: GetAllRoutineUseCase,
) : ViewModel(), RoutineHistoryContract {

  private val mutableState = MutableStateFlow(RoutineHistoryContract.HistoryState())
  override val state: StateFlow<RoutineHistoryContract.HistoryState> = mutableState.onStart {
    getAllRoutine()
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), RoutineHistoryContract.HistoryState())

  override fun event(event: RoutineHistoryContract.HistoryEvent) = when (event) {
    RoutineHistoryContract.HistoryEvent.GetAllRoutine -> getAllRoutine()
  }

  private fun getAllRoutine() {
    viewModelScope.launch {
      mutableState.update {
        it.copy(routines = getRoutineUseCase.invoke())
      }
    }
  }
}
