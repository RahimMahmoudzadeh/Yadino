package com.rahim.yadino.note.presentation.component.addNoteDialog

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.rahim.yadino.note.domain.useCase.AddNoteUseCase
import com.rahim.yadino.note.domain.useCase.UpdateNoteUseCase
import com.rahim.yadino.note.presentation.mapper.toNote
import com.rahim.yadino.note.presentation.model.NoteUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class AddNoteDialogComponentImpl(
  componentContext: ComponentContext,
  mainDispatcher: CoroutineContext,
  ioDispatcher: CoroutineContext,
  private val addNoteUseCase: AddNoteUseCase,
  private val updateNoteUseCase: UpdateNoteUseCase,
  private val updateNote: NoteUiModel?,
  private val onDismissed: () -> Unit,
) : AddNoteDialogComponent, ComponentContext by componentContext {

  private val mainScope: CoroutineScope = coroutineScope(mainDispatcher + SupervisorJob())
  private val ioScope: CoroutineScope = coroutineScope(ioDispatcher + SupervisorJob())


  private val _state = MutableValue(AddNoteDialogComponent.State(updateNote = updateNote))
  override val state: Value<AddNoteDialogComponent.State> = _state

  override fun event(event: AddNoteDialogComponent.Event) = when (event) {
    is AddNoteDialogComponent.Event.CreateNote -> addNote(event.note)
    AddNoteDialogComponent.Event.Dismiss -> onDismissed()
    is AddNoteDialogComponent.Event.UpdateNote -> updateNote(event.note)
  }

  private fun addNote(note: NoteUiModel) {
    mainScope.launch {
      addNoteUseCase(note.toNote())
      onDismissed()
    }
  }

  private fun updateNote(note: NoteUiModel) {
    mainScope.launch {
      updateNoteUseCase(note.toNote())
      onDismissed()
    }
  }
}
