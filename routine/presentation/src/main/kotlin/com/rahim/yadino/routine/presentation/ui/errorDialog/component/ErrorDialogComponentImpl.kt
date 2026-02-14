package com.rahim.yadino.routine.presentation.ui.errorDialog.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.rahim.yadino.routine.presentation.model.ErrorDialogUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ErrorDialogComponentImpl(
  componentContext: ComponentContext,
  mainContext: CoroutineContext,
  errorDialogUiModel: ErrorDialogUiModel,
  private val onDismissed: () -> Unit,
) : ErrorDialogComponent, ComponentContext by componentContext {

  private val scope: CoroutineScope = coroutineScope(mainContext + SupervisorJob())

  private val _state = MutableValue(ErrorDialogComponent.State(title = errorDialogUiModel.title, submitTextButton = errorDialogUiModel.submitTextButton))
  override val state: Value<ErrorDialogComponent.State> = _state

  private val _effect = Channel<ErrorDialogComponent.Effect>(Channel.BUFFERED)
  override val effects: Flow<ErrorDialogComponent.Effect> = _effect.receiveAsFlow()

  override fun onEvent(event: ErrorDialogComponent.Event) = when (event) {
    ErrorDialogComponent.Event.CancelClicked -> onDismissed()
    ErrorDialogComponent.Event.OkClicked -> {
      okClickedButton()
    }

    ErrorDialogComponent.Event.Dismissed -> onDismissed()
  }

  private fun okClickedButton() {
    scope.launch {
      _effect.send(ErrorDialogComponent.Effect.NavigateToSettingPermissionPoshNotification)
    }
  }
}
