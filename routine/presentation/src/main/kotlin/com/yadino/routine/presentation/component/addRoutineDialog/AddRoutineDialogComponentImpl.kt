package com.yadino.routine.presentation.component.addRoutineDialog

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import kotlin.coroutines.CoroutineContext

class AddRoutineDialogComponentImpl(componentContext: ComponentContext, mainDispatcher: CoroutineContext, onDismissed: () -> Unit) : AddRoutineDialogComponent, ComponentContext by componentContext {
  private val _state = MutableValue(AddRoutineDialogComponent.State())
  override val state: Value<AddRoutineDialogComponent.State> = _state

  override fun event(event: AddRoutineDialogComponent.Event) {
    TODO("Not yet implemented")
  }
}
