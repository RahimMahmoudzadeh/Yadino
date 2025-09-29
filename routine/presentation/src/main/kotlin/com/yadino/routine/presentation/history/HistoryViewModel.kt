package com.yadino.routine.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahim.yadino.base.LoadableData
import com.yadino.routine.domain.useCase.GetAllRoutineUseCase
import com.yadino.routine.presentation.mapper.toRoutinePresentationLayer
import com.yadino.routine.presentation.model.IncompleteOrCompletedRoutines
import com.yadino.routine.presentation.model.RoutinePresentationLayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
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
        it.copy(
          incompleteOrCompletedRoutines = LoadableData.Loading,
        )
      }
      val routines = getRoutineUseCase().map { it.toRoutinePresentationLayer() }
      val (completedRoutine, incompleteRoutine) = if (routines.isNotEmpty()) {
        routines.partition { sort -> sort.isChecked }
      } else {
        persistentListOf<RoutinePresentationLayer>() to persistentListOf()
      }
      mutableState.update {
        it.copy(
          incompleteOrCompletedRoutines = LoadableData.Loaded(
            IncompleteOrCompletedRoutines(
              incompleteRoutine = incompleteRoutine.toPersistentList(),
              completedRoutine = completedRoutine.toPersistentList(),
            ),
          ),
        )
      }
    }
  }
}
