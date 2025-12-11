package com.rahim.yadino.home.presentation.component.addRoutineDialog

import androidx.compose.runtime.Immutable
import com.rahim.yadino.base.UnidirectionalComponent
import com.rahim.yadino.enums.message.MessageUi
import com.rahim.yadino.home.domain.model.Routine
import com.rahim.yadino.home.presentation.model.RoutineUiModel

interface AddRoutineDialogComponent : UnidirectionalComponent<AddRoutineDialogComponent.Event, AddRoutineDialogComponent.State, AddRoutineDialogComponent.EFFECT> {

  @Immutable
  sealed class Event {
    data object DismissDialog : Event()
    data class CreateRoutine(val routine: RoutineUiModel) : Event()
    data class UpdateRoutine(val routine: RoutineUiModel) : Event()
  }

  @Immutable
  sealed class EFFECT {
    data class ShowToast(val messageUi: MessageUi) : EFFECT()
  }

  @Immutable
  data class State(val data: String = "", val updateRoutine: RoutineUiModel? = null)
}
