package com.yadino.routine.presentation.history

import androidx.compose.runtime.Immutable
import com.rahim.yadino.base.LoadableData
import com.rahim.yadino.base.UnidirectionalComponent
import com.rahim.yadino.base.UnidirectionalViewModel
import com.rahim.yadino.enums.error.ErrorMessageCode
import com.yadino.routine.presentation.model.IncompleteOrCompletedRoutinesUiModel

interface RoutineHistoryContract : UnidirectionalViewModel<RoutineHistoryContract.HistoryEvent, RoutineHistoryContract.HistoryState> {
  @Immutable
  sealed class HistoryEvent() {
    data object GetAllRoutine : HistoryEvent()
  }

  @Immutable
  data class HistoryState(
    val incompleteOrCompletedRoutinesUiModel: LoadableData<IncompleteOrCompletedRoutinesUiModel> = LoadableData.Initial,
    val errorMessage: ErrorMessageCode? = null,
  )
}
