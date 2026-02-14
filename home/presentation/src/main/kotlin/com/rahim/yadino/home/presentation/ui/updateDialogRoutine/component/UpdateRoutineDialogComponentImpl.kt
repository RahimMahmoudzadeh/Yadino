package com.rahim.yadino.home.presentation.ui.updateDialogRoutine.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.rahim.yadino.base.toMessageUi
import com.rahim.yadino.enums.message.MessageUi
import com.rahim.yadino.home.domain.useCase.UpdateReminderUseCase
import com.rahim.yadino.home.presentation.mapper.toRoutine
import com.rahim.yadino.home.presentation.model.RoutineUiModel
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class UpdateRoutineDialogComponentImpl(
  componentContext: ComponentContext,
  mainDispatcher: CoroutineContext,
  private val updateReminderUseCase: UpdateReminderUseCase,
  private val updateRoutine: RoutineUiModel,
  private val onDismissed: () -> Unit,
) : UpdateRoutineDialogComponent, ComponentContext by componentContext {

  private val scope = coroutineScope(mainDispatcher + SupervisorJob())

  private val _state = MutableValue(UpdateRoutineDialogComponent.State(updateRoutine = updateRoutine))
  override val state: Value<UpdateRoutineDialogComponent.State> = _state


  private val _effect: Channel<UpdateRoutineDialogComponent.Effect> = Channel(Channel.BUFFERED)
  override val effect: Flow<UpdateRoutineDialogComponent.Effect> = _effect.receiveAsFlow()

  override fun event(event: UpdateRoutineDialogComponent.Event) = when (event) {
    UpdateRoutineDialogComponent.Event.DismissDialog -> onDismissed()
    is UpdateRoutineDialogComponent.Event.UpdateRoutine -> updateRoutine(event.routine)
  }

  private fun updateRoutine(routine: RoutineUiModel) {
    scope.launch {
      runCatching {
        updateReminderUseCase.invoke(routine.toRoutine())
      }.onSuccess {
        _effect.send(UpdateRoutineDialogComponent.Effect.ShowToast(it.toMessageUi(onDismissed)))
      }.onFailure {
        _effect.send(UpdateRoutineDialogComponent.Effect.ShowToast(MessageUi.ERROR_UPDATE_REMINDER))
      }
    }
  }
}
