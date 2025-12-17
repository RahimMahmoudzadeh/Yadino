package com.rahim.yadino.designsystem.dialog.error.component

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.rahim.yadino.base.UnidirectionalComponent
import com.rahim.yadino.enums.message.success.SuccessMessage

interface ErrorComponent : UnidirectionalComponent<ErrorComponent.Event, ErrorComponent.State, ErrorComponent.Effect> {
  @Immutable
  sealed class Event {
    object DismissDialog : Event()
  }

  @Immutable
  sealed class Effect {
    data class ShowToast(val message: SuccessMessage) : Effect()
  }

  @Stable
  data class State(val date: String="")
}
