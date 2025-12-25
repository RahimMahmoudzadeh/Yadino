package com.rahim.yadino.routine.presentation.component.addRoutineDialog

import androidx.compose.runtime.Immutable
import com.rahim.yadino.base.UnidirectionalComponent
import com.rahim.yadino.enums.message.MessageUi
import com.rahim.yadino.routine.presentation.model.CurrentTimeUiModel
import com.rahim.yadino.routine.presentation.model.IncreaseDecrease
import com.rahim.yadino.routine.presentation.model.RoutineUiModel
import com.rahim.yadino.routine.presentation.model.TimeDateUiModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

interface AddRoutineDialogComponent : UnidirectionalComponent<AddRoutineDialogComponent.Event, AddRoutineDialogComponent.State, AddRoutineDialogComponent.Effect> {

  @Immutable
  sealed class Event {
    data object Dismiss : Event()
    data class CreateRoutine(val routine: RoutineUiModel) : Event()
    data class MonthChange(val yearNumber: Int, val monthNumber: Int, val increaseDecrease: IncreaseDecrease) : Event()
  }

  @Immutable
  sealed class Effect {
    data class ShowToast(val messageUi: MessageUi): Effect()
  }

  @Immutable
  data class State(
    val timesMonth: PersistentList<TimeDateUiModel> = persistentListOf(),
    val currentTime: CurrentTimeUiModel? = null,
  )
}
