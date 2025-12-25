package com.rahim.yadino.note.presentation.component.updateNoteDialog

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.rahim.yadino.enums.message.MessageUi
import com.rahim.yadino.note.domain.useCase.AddNoteUseCase
import com.rahim.yadino.note.domain.useCase.UpdateNoteUseCase
import com.rahim.yadino.note.presentation.mapper.toNote
import com.rahim.yadino.note.presentation.model.NoteUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class UpdateNoteDialogComponentImpl(
  componentContext: ComponentContext,
  mainDispatcher: CoroutineContext,
  ioDispatcher: CoroutineContext,
  private val updateNoteUseCase: UpdateNoteUseCase,
  private val updateNote: NoteUiModel?,
  private val onDismissed: () -> Unit,
) : UpdateNoteDialogComponent, ComponentContext by componentContext {

  private val mainScope: CoroutineScope = coroutineScope(mainDispatcher + SupervisorJob())
  private val ioScope: CoroutineScope = coroutineScope(ioDispatcher + SupervisorJob())


  final override val state: Value<UpdateNoteDialogComponent.State>
    field = MutableValue(UpdateNoteDialogComponent.State(updateNote = updateNote))

  private val _effect = Channel<UpdateNoteDialogComponent.Effect>(Channel.BUFFERED)
  override val effect: Flow<UpdateNoteDialogComponent.Effect> = _effect.consumeAsFlow()

  override fun event(event: UpdateNoteDialogComponent.Event) = when (event) {
    UpdateNoteDialogComponent.Event.Dismiss -> onDismissed()
    is UpdateNoteDialogComponent.Event.UpdateNote -> updateNote(event.note)
  }

  private fun updateNote(note: NoteUiModel) {
    mainScope.launch {
      updateNoteUseCase(note.toNote())
      onDismissed()
    }
  }
}
