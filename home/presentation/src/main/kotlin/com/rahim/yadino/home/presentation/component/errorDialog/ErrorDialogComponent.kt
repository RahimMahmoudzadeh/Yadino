package com.rahim.yadino.home.presentation.component.errorDialog

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.rahim.yadino.base.UnidirectionalComponent
import com.rahim.yadino.enums.message.MessageUi
import com.rahim.yadino.enums.message.success.SuccessMessage

interface ErrorDialogComponent : UnidirectionalComponent<ErrorDialogComponent.Event, ErrorDialogComponent.State, ErrorDialogComponent.Effect> {
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
