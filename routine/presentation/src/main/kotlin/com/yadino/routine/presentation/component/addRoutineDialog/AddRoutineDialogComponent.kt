package com.yadino.routine.presentation.component.addRoutineDialog

import androidx.compose.runtime.Immutable
import com.rahim.yadino.base.UnidirectionalComponent
import com.yadino.routine.presentation.component.RoutineComponent.Event
import com.yadino.routine.presentation.model.IncreaseDecrease
import com.yadino.routine.presentation.model.RoutineUiModel
import com.yadino.routine.presentation.model.TimeDateUiModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

interface AddRoutineDialogComponent : UnidirectionalComponent<AddRoutineDialogComponent.Event, AddRoutineDialogComponent.State> {

  @Immutable
  sealed class Event {
    data object Dismiss : Event()
    data class CreateRoutine(val routine: RoutineUiModel) : Event()
    data class MonthChange(val yearNumber: Int, val monthNumber: Int, val increaseDecrease: IncreaseDecrease) : Event()

  }

  @Immutable
  data class State(val timesMonth: PersistentList<TimeDateUiModel> = persistentListOf())

}
