package com.rahim.yadino.routine.presentation.ui.alarmHistory.component

import androidx.compose.runtime.Immutable
import com.rahim.yadino.base.LoadableData
import com.rahim.yadino.base.UnidirectionalComponent
import com.rahim.yadino.routine.presentation.model.IncompleteOrCompletedRoutinesUiModel

interface HistoryRoutineComponent : UnidirectionalComponent<HistoryRoutineComponent.Event, HistoryRoutineComponent.HistoryState, Nothing> {

  @Immutable
  sealed class Event {

  }

  @Immutable
  data class HistoryState(
    val incompleteOrCompletedRoutinesUiModel: LoadableData<IncompleteOrCompletedRoutinesUiModel> = LoadableData.Initial,
  )
}
