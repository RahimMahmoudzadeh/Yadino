package com.rahim.yadino.routine.presentation.ui.errorDialog.component

import androidx.compose.runtime.Immutable
import com.rahim.yadino.base.UnidirectionalComponent
import com.rahim.yadino.enums.message.MessageUi

interface ErrorDialogComponent : UnidirectionalComponent<ErrorDialogComponent.Event, ErrorDialogComponent.State, ErrorDialogComponent.Effect> {
  @Immutable
  sealed class Event {
    object OkClicked : Event()
    object CancelClicked : Event()
    object Dismissed : Event()
  }

  @Immutable
  sealed interface Effect {
    data class ShowToast(val messageUi: MessageUi) : Effect
    data object NavigateToSettingPermissionPoshNotification : Effect
  }

  @Immutable
  data class State(val title: String,val submitTextButton: String)
}
