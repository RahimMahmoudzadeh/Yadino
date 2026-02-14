package com.rahim.yadino.routine.presentation.ui.alarmHistory.component

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.rahim.yadino.base.LoadableData
import com.rahim.yadino.base.StateSource
import com.rahim.yadino.base.UnidirectionalComponent
import com.rahim.yadino.routine.presentation.model.IncompleteOrCompletedRoutinesUiModel

interface HistoryRoutineComponent : StateSource<HistoryRoutineComponent.HistoryState> {

  @Stable
  data class HistoryState(
    val incompleteOrCompletedRoutinesUiModel: LoadableData<IncompleteOrCompletedRoutinesUiModel> = LoadableData.Initial,
  )
}
