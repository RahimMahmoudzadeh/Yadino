package com.rahim.yadino.note.presentation.ui.root

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.rahim.yadino.note.presentation.ui.addNoteDialog.AddNoteDialog
import com.rahim.yadino.note.presentation.ui.errorDialog.ErrorDialogUi
import com.rahim.yadino.note.presentation.ui.main.NoteMainScreen
import com.rahim.yadino.note.presentation.ui.root.component.RootNoteComponent
import com.rahim.yadino.note.presentation.ui.updateNoteDialog.UpdateNoteDialog

@Composable
fun NoteRoute(
  modifier: Modifier = Modifier,
  clickSearch: Boolean,
  component: RootNoteComponent,
) {

  val updateNoteDialog = component.updateNoteDialogScreen.subscribeAsState().value.child
  val errorDialogNote = component.errorDialogRemoveNoteScreen.subscribeAsState().value.child

  updateNoteDialog?.let { dialogSlot ->
    dialogSlot.instance.also { dialogComponent ->
      UpdateNoteDialog(
        component = dialogComponent,
      )
    }
  }

  errorDialogNote?.let { dialogSlot ->
    dialogSlot.instance.also { dialogComponent ->
      ErrorDialogUi(
        component = dialogComponent,
      )
    }
  }

  Children(
    stack = component.stack,
    modifier = modifier.fillMaxSize(),
    animation = stackAnimation(fade()),
  ) {
    when (val child = it.instance) {
      is RootNoteComponent.ChildStack.NoteMainStack -> {
        NoteMainScreen(clickSearch = clickSearch, component = child.component)
      }
    }
  }
}


