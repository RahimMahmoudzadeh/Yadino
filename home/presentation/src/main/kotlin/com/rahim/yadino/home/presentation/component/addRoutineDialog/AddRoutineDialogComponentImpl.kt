package com.rahim.yadino.home.presentation.component.addRoutineDialog

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.rahim.yadino.home.domain.useCase.AddReminderUseCase
import com.rahim.yadino.home.presentation.mapper.toRoutine
import com.rahim.yadino.home.presentation.model.RoutineUiModel
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class AddRoutineDialogComponentImpl(
  componentContext: ComponentContext,
  mainDispatcher: CoroutineContext,
  private val addReminderUseCase: AddReminderUseCase,
  private val routine: RoutineUiModel?,
  private val onDismissed: () -> Unit,
) : AddRoutineDialogComponent, ComponentContext by componentContext {

  private val scope = coroutineScope(mainDispatcher + SupervisorJob())

  private val _state = MutableValue(AddRoutineDialogComponent.State(updateRoutine = routine))
  override val state: Value<AddRoutineDialogComponent.State> = _state

  override fun event(event: AddRoutineDialogComponent.Event) = when (event) {
    AddRoutineDialogComponent.Event.DismissDialog -> onDismissed()
    is AddRoutineDialogComponent.Event.CreateRoutine -> addRoutine(event.routine)
  }

  private fun addRoutine(routine: RoutineUiModel) {
    scope.launch {
      runCatching {
        addReminderUseCase.invoke(routine.toRoutine())
      }.onSuccess {
        onDismissed()
      }.onFailure {

      }
    }
  }
}
