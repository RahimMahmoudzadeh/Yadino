package com.rahim.yadino.designsystem.dialog.error.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlin.coroutines.CoroutineContext

class ErrorComponentImpl(
  mainContext: CoroutineContext,
  componentContext: ComponentContext,
  private val itemId: String,
  private val message: String,
  private val okMessage: String,
  private val onDissmiss: () -> Unit,
) : ErrorComponent, ComponentContext by componentContext {

  private val scope: CoroutineScope = coroutineScope(mainContext + SupervisorJob())

  private val _state = MutableValue(ErrorComponent.State())
  override val state: Value<ErrorComponent.State> = _state

  private val _effect = Channel<ErrorComponent.Effect>(Channel.BUFFERED)
  override val effect: Flow<ErrorComponent.Effect> = _effect.receiveAsFlow()

  override fun event(event: ErrorComponent.Event) = when (event) {
    ErrorComponent.Event.DismissDialog -> onDissmiss()
  }

}
