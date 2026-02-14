package com.rahim.yadino.note.presentation.ui.errorDialog.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.rahim.yadino.enums.message.MessageUi
import com.rahim.yadino.note.domain.useCase.DeleteNoteUseCase
import com.rahim.yadino.note.presentation.mapper.toNote
import com.rahim.yadino.note.presentation.model.ErrorDialogRemoveNoteUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ErrorDialogComponentImpl(
  mainContext: CoroutineContext,
  componentContext: ComponentContext,
  private val deleteNoteUseCase: DeleteNoteUseCase,
  private val errorDialogRemoveNoteUiModel: ErrorDialogRemoveNoteUiModel,
  private val onDismissed: () -> Unit,
) : ErrorDialogComponent, ComponentContext by componentContext {

  private val scope: CoroutineScope = coroutineScope(mainContext + SupervisorJob())

  private val _state = MutableValue(ErrorDialogComponent.State(title = errorDialogRemoveNoteUiModel.title, submitTextButton = errorDialogRemoveNoteUiModel.submitTextButton))
  override val state: Value<ErrorDialogComponent.State> = _state

  private val _effect = Channel<ErrorDialogComponent.Effect>(Channel.BUFFERED)
  override val effect: Flow<ErrorDialogComponent.Effect> = _effect.receiveAsFlow()

  override fun event(event: ErrorDialogComponent.Event) = when (event) {
    ErrorDialogComponent.Event.CancelClicked -> onDismissed()
    ErrorDialogComponent.Event.OkClicked -> {
      okClickedButton()
    }
  }

  private fun okClickedButton() {
    scope.launch {
      runCatching {
        deleteNoteUseCase(errorDialogRemoveNoteUiModel.noteUiModel.toNote())
      }.onSuccess {
        onDismissed()
      }.onFailure {
        _effect.send(ErrorDialogComponent.Effect.ShowToast(MessageUi.ERROR_REMOVE_REMINDER))
      }
    }
  }
}
