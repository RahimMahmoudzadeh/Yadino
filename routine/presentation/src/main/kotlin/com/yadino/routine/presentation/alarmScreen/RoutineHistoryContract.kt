package com.yadino.routine.presentation.alarmScreen

import androidx.compose.runtime.Immutable
import com.rahim.yadino.base.UnidirectionalViewModel
import com.rahim.yadino.enums.error.ErrorMessageCode
import com.yadino.routine.domain.model.RoutineDomainLayer

interface RoutineHistoryContract : UnidirectionalViewModel<RoutineHistoryContract.HistoryEvent, RoutineHistoryContract.HistoryState> {
  @Immutable
  sealed class HistoryEvent() {
    data object GetAllRoutine : HistoryEvent()
  }

  @Immutable
  data class HistoryState(
      val routineLoading: Boolean = true,
      val routines: List<RoutineDomainLayer> = emptyList(),
      val errorMessage: ErrorMessageCode? = null,
  )
}
