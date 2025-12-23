package com.rahim.yadino.home.presentation.component.addRoutineDialog

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.rahim.yadino.base.Resource
import com.rahim.yadino.base.toMessageUi
import com.rahim.yadino.enums.message.MessageUi
import com.rahim.yadino.enums.message.error.ErrorMessage
import com.rahim.yadino.enums.message.success.SuccessMessage
import com.rahim.yadino.home.domain.useCase.AddReminderUseCase
import com.rahim.yadino.home.domain.useCase.UpdateReminderUseCase
import com.rahim.yadino.home.presentation.mapper.toRoutine
import com.rahim.yadino.home.presentation.model.RoutineUiModel
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class AddRoutineDialogComponentImpl(
  componentContext: ComponentContext,
  mainDispatcher: CoroutineContext,
  private val addReminderUseCase: AddReminderUseCase,
  private val updateReminderUseCase: UpdateReminderUseCase,
  private val updateRoutine: RoutineUiModel?,
  private val onDismissed: () -> Unit,
) : AddRoutineDialogComponent, ComponentContext by componentContext {

  private val scope = coroutineScope(mainDispatcher + SupervisorJob())

  private val _state = MutableValue(AddRoutineDialogComponent.State(updateRoutine = updateRoutine))
  override val state: Value<AddRoutineDialogComponent.State> = _state


  private val _effect: Channel<AddRoutineDialogComponent.Effect> = Channel(Channel.BUFFERED)
  override val effect: Flow<AddRoutineDialogComponent.Effect> = _effect.receiveAsFlow()

  override fun event(event: AddRoutineDialogComponent.Event) = when (event) {
    AddRoutineDialogComponent.Event.DismissDialog -> onDismissed()
    is AddRoutineDialogComponent.Event.CreateRoutine -> addRoutine(event.routine)
    is AddRoutineDialogComponent.Event.UpdateRoutine -> updateRoutine(event.routine)
  }

  private fun addRoutine(routine: RoutineUiModel) {
    scope.launch {
      runCatching {
        addReminderUseCase(routine.toRoutine())
      }.onSuccess {
        when(it){
          is Resource.Error<ErrorMessage> -> {
            _effect.send(AddRoutineDialogComponent.Effect.ShowToast(it.error.toMessageUi()))
          }
          is Resource.Success<SuccessMessage> ->  {
            _effect.send(AddRoutineDialogComponent.Effect.ShowToast(it.data.toMessageUi()))
            delay(100)
            onDismissed()
          }
        }
      }.onFailure {
        _effect.send(AddRoutineDialogComponent.Effect.ShowToast(MessageUi.ERROR_SAVE_REMINDER))
      }
    }
  }

  private fun updateRoutine(routine: RoutineUiModel) {
    scope.launch {
      runCatching {
        updateReminderUseCase.invoke(routine.toRoutine())
      }.onSuccess {
        _effect.send(AddRoutineDialogComponent.Effect.ShowToast(MessageUi.SUCCESS_UPDATE_REMINDER))
        onDismissed()
      }.onFailure {
        _effect.send(AddRoutineDialogComponent.Effect.ShowToast(MessageUi.ERROR_UPDATE_REMINDER))
      }
    }
  }
}
