package com.rahim.yadino.note.presentation.ui.addNoteDialog.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.rahim.yadino.enums.message.MessageUi
import com.rahim.yadino.note.domain.useCase.AddNoteUseCase
import com.rahim.yadino.note.presentation.mapper.toNote
import com.rahim.yadino.note.presentation.model.NoteUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class AddNoteDialogComponentImpl(
  componentContext: ComponentContext,
  mainDispatcher: CoroutineContext,
  ioDispatcher: CoroutineContext,
  private val addNoteUseCase: AddNoteUseCase,
  private val onDismissed: () -> Unit,
) : AddNoteDialogComponent, ComponentContext by componentContext {

  private val mainScope: CoroutineScope = coroutineScope(mainDispatcher + SupervisorJob())
  private val ioScope: CoroutineScope = coroutineScope(ioDispatcher + SupervisorJob())


  private val _state = MutableValue(AddNoteDialogComponent.State())
  final override val state: Value<AddNoteDialogComponent.State> = _state

  private val _effects = Channel<AddNoteDialogComponent.Effect>(Channel.BUFFERED)
  override val effects: Flow<AddNoteDialogComponent.Effect> = _effects.consumeAsFlow()

  override fun onEvent(event: AddNoteDialogComponent.Event) = when (event) {
    is AddNoteDialogComponent.Event.CreateNote -> addNote(event.note)
    AddNoteDialogComponent.Event.Dismiss -> onDismissed()
  }

  private fun addNote(note: NoteUiModel) {
    mainScope.launch {
      runCatching {
        addNoteUseCase(note.toNote())
      }.onSuccess {
        _effects.send(AddNoteDialogComponent.Effect.ShowToast(MessageUi.SUCCESS_ADD_NOTE))
        onDismissed()
      }.onFailure {
        _effects.send(AddNoteDialogComponent.Effect.ShowToast(MessageUi.ERROR_ADD_NOTE))
      }
    }
  }
}
