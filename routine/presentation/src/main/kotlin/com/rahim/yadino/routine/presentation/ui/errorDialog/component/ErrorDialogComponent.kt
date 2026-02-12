package com.rahim.yadino.routine.presentation.ui.errorDialog.component

import androidx.compose.runtime.Immutable
import com.rahim.yadino.base.UnidirectionalComponent
import com.rahim.yadino.enums.message.MessageUi

interface ErrorDialogComponent : UnidirectionalComponent<ErrorDialogComponent.Event, ErrorDialogComponent.State, ErrorDialogComponent.State> {
  @Immutable
  sealed class Event {
    data object DismissDialog : Event()
  }

  @Immutable
  sealed class Effect {
    data class ShowToast(val messageUi: MessageUi) : Effect()
  }

  @Immutable
  data class State(val data: String = "")
}
