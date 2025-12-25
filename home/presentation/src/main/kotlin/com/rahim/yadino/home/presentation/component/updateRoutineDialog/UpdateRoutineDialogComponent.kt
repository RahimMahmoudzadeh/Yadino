package com.rahim.yadino.home.presentation.component.updateRoutineDialog

import androidx.compose.runtime.Immutable
import com.rahim.yadino.base.UnidirectionalComponent
import com.rahim.yadino.enums.message.MessageUi
import com.rahim.yadino.home.presentation.model.RoutineUiModel

interface UpdateRoutineDialogComponent : UnidirectionalComponent<UpdateRoutineDialogComponent.Event, UpdateRoutineDialogComponent.State, UpdateRoutineDialogComponent.Effect> {

  @Immutable
  sealed class Event {
    data object DismissDialog : Event()
    data class UpdateRoutine(val routine: RoutineUiModel) : Event()
  }

  @Immutable
  sealed class Effect {
    data class ShowToast(val messageUi: MessageUi) : Effect()
  }

  @Immutable
  data class State(val data: String = "", val updateRoutine: RoutineUiModel? = null)
}
