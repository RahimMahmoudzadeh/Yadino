package com.rahim.yadino.home.presentation.ui.errorDialog.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.rahim.yadino.home.presentation.model.ErrorDialogUiModel
import kotlin.coroutines.CoroutineContext

class ErrorDialogComponentImpl(
  componentContext: ComponentContext,
  mainContext: CoroutineContext,
  errorDialogUiModel: ErrorDialogUiModel,
  onDismissed: () -> Unit,
) : ErrorDialogComponent, ComponentContext by componentContext {

  private val _state = MutableValue(ErrorDialogComponent.State())
  override val state: Value<ErrorDialogComponent.State> = _state




}
