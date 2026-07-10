package com.rahim.ui.errorDialog.component

//class ErrorDialogComponentImpl(
//  componentContext: ComponentContext,
//  mainContext: CoroutineContext,
//  errorDialogUiModel: ErrorDialogUiModel,
//  private val onDismissed: () -> Unit,
//) : ErrorDialogComponent, ComponentContext by componentContext {
//
//  private val scope: CoroutineScope = coroutineScope(mainContext + SupervisorJob())
//
//  private val _state = MutableValue(ErrorDialogComponent.State(title = errorDialogUiModel.title, submitTextButton = errorDialogUiModel.submitTextButton))
//  override val state: Value<ErrorDialogComponent.State> = _state
//
//
//  private val _effect = Channel<ErrorDialogComponent.Effect>(Channel.BUFFERED)
//  override val effects: Flow<ErrorDialogComponent.Effect> = _effect.receiveAsFlow()
//
//  override fun onEvent(event: ErrorDialogComponent.Event) = when (event) {
//    ErrorDialogComponent.Event.CancelClicked -> onDismissed()
//    ErrorDialogComponent.Event.OkClicked -> {
//      okClickedButton()
//    }
//    ErrorDialogComponent.Event.Dismissed -> onDismissed()
//  }
//
//  private fun okClickedButton() {
//    scope.launch {
//      _effect.send(ErrorDialogComponent.Effect.NavigateToSettingPermissionPoshNotification)
//    }
//  }
//}
