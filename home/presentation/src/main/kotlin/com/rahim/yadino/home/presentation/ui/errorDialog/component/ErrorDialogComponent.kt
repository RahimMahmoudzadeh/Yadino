package com.rahim.yadino.home.presentation.ui.errorDialog.component

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.rahim.yadino.base.UnidirectionalComponent
import com.rahim.yadino.enums.message.MessageUi
import com.rahim.yadino.home.presentation.ui.errorDialogRemoveRoutine.component.ErrorDialogRemoveRoutineComponent.Event

interface ErrorDialogComponent : UnidirectionalComponent<ErrorDialogComponent.Event, ErrorDialogComponent.State, ErrorDialogComponent.Effect> {
  @Immutable
  sealed interface Event {
    object OkClicked : Event
    object CancelClicked : Event
    object Dismissed : Event
  }

  @Immutable
  sealed interface Effect {
    data class ShowToast(val messageUi: MessageUi) : Effect
    data object NavigateToSettingPermissionPoshNotification : Effect
  }

  @Stable
  data class State(val title: String,val submitTextButton: String)
}
