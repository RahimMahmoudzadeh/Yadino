package com.rahim.yadino.note.presentation.component.addNoteDialog

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.rahim.yadino.base.UnidirectionalComponent
import kotlin.coroutines.CoroutineContext

class AddNoteDialogComponentImpl(componentContext: ComponentContext, mainDispatcher: CoroutineContext, ioDispatcher: CoroutineContext) : AddNoteDialogComponent, ComponentContext by componentContext {

  private val _state = MutableValue(AddNoteDialogComponent.State())
  override val state: Value<AddNoteDialogComponent.State> = _state

  override fun event(event: AddNoteDialogComponent.Event) = when (event) {
    is AddNoteDialogComponent.Event.CreateNote -> TODO()
    AddNoteDialogComponent.Event.Dismiss -> TODO()
    is AddNoteDialogComponent.Event.UpdateNote -> TODO()
  }


}
