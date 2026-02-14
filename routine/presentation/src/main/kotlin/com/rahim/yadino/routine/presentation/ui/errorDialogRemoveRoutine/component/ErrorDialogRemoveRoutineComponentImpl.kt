package com.rahim.yadino.routine.presentation.ui.errorDialogRemoveRoutine.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.rahim.yadino.enums.message.MessageUi
import com.rahim.yadino.routine.domain.useCase.DeleteReminderUseCase
import com.rahim.yadino.routine.presentation.mapper.toRoutine
import com.rahim.yadino.routine.presentation.model.ErrorDialogRemoveRoutineUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ErrorDialogRemoveRoutineComponentImpl(
  mainContext: CoroutineContext,
  componentContext: ComponentContext,
  private val deleteReminderUseCase: DeleteReminderUseCase,
  private val errorDialogRemoveRoutineUiModel: ErrorDialogRemoveRoutineUiModel,
  private val onDismissed: () -> Unit,
) : ErrorDialogRemoveRoutineComponent, ComponentContext by componentContext {

  private val scope: CoroutineScope = coroutineScope(mainContext + SupervisorJob())

  private val _state = MutableValue(ErrorDialogRemoveRoutineComponent.State(title = errorDialogRemoveRoutineUiModel.title, submitTextButton = errorDialogRemoveRoutineUiModel.submitTextButton))
  override val state: Value<ErrorDialogRemoveRoutineComponent.State> = _state

  private val _effect = Channel<ErrorDialogRemoveRoutineComponent.Effect>(Channel.BUFFERED)
  override val effects: Flow<ErrorDialogRemoveRoutineComponent.Effect> = _effect.receiveAsFlow()

  override fun onEvent(event: ErrorDialogRemoveRoutineComponent.Event) = when (event) {
    ErrorDialogRemoveRoutineComponent.Event.CancelClicked -> onDismissed()
    ErrorDialogRemoveRoutineComponent.Event.OkClicked -> {
      okClickedButton()
    }
  }

  private fun okClickedButton() {
    scope.launch {
      runCatching {
        deleteReminderUseCase(errorDialogRemoveRoutineUiModel.routineUiModel.toRoutine())
      }.onSuccess {
        onDismissed()
      }.onFailure {
        _effect.send(ErrorDialogRemoveRoutineComponent.Effect.ShowToast(MessageUi.ERROR_REMOVE_REMINDER))
      }
    }
  }
}
