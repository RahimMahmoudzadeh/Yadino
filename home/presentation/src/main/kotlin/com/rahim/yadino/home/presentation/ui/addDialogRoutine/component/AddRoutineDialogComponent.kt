package com.rahim.yadino.home.presentation.ui.addDialogRoutine.component

import androidx.compose.runtime.Immutable
import com.rahim.yadino.base.UnidirectionalComponent
import com.rahim.yadino.enums.message.MessageUi
import com.rahim.yadino.home.presentation.model.RoutineUiModel

interface AddRoutineDialogComponent : UnidirectionalComponent<AddRoutineDialogComponent.Event, AddRoutineDialogComponent.State, AddRoutineDialogComponent.Effect> {

  @Immutable
  sealed class Event {
    data object DismissDialog : Event()
    data class CreateRoutine(val routine: RoutineUiModel) : Event()
  }

  @Immutable
  sealed class Effect {
    data class ShowToast(val messageUi: MessageUi) : Effect()
  }

  @Immutable
  data class State(val data: String = "",)
}
