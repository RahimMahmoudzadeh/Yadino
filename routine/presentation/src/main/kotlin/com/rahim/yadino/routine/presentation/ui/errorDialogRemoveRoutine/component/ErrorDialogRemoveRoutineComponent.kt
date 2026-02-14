package com.rahim.yadino.routine.presentation.ui.errorDialogRemoveRoutine.component

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.rahim.yadino.base.UnidirectionalComponent
import com.rahim.yadino.enums.message.MessageUi

interface ErrorDialogRemoveRoutineComponent : UnidirectionalComponent<ErrorDialogRemoveRoutineComponent.Event, ErrorDialogRemoveRoutineComponent.State, ErrorDialogRemoveRoutineComponent.Effect> {
  @Immutable
  sealed class Event {
    object OkClicked : Event()
    object CancelClicked : Event()
  }

  @Immutable
  sealed class Effect {
    data class ShowToast(val message: MessageUi) : Effect()
  }

  @Stable
  data class State(val title: String,val submitTextButton: String)
}
