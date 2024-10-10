package com.rahim.yadino.routine.alarmScreen

import androidx.compose.runtime.Immutable
import com.rahim.yadino.base.UnidirectionalViewModel
import com.rahim.yadino.enums.error.ErrorMessageCode
import com.rahim.yadino.model.RoutineModel

interface RoutineHistoryContract : UnidirectionalViewModel<RoutineHistoryContract.HistoryEvent, RoutineHistoryContract.HistoryState> {
  @Immutable
  sealed class HistoryEvent() {
    data object GetAllRoutine : HistoryEvent()
  }

  @Immutable
  data class HistoryState(
    val routineLoading: Boolean = true,
    val routines: List<RoutineModel> = emptyList(),
    val errorMessage: ErrorMessageCode? = null,
  )
}
