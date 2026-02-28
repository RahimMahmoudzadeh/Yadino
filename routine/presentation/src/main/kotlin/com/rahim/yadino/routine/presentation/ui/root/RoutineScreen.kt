package com.rahim.yadino.routine.presentation.ui.root

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.rahim.yadino.routine.presentation.ui.addRoutineDialog.AddRoutineDialog
import com.rahim.yadino.routine.presentation.ui.errorDialog.ErrorDialogUi
import com.rahim.yadino.routine.presentation.ui.errorDialogRemoveRoutine.ErrorDialogRemoveRoutineUi
import com.rahim.yadino.routine.presentation.ui.main.RoutineMainUi
import com.rahim.yadino.routine.presentation.ui.root.component.RootRoutineComponent
import com.rahim.yadino.routine.presentation.ui.updateDialogRoutine.UpdateRoutineDialog

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RoutineRoute(
  modifier: Modifier = Modifier,
  showSearchBar: Boolean,
  component: RootRoutineComponent,
) {

  Children(
    stack = component.stack,
    modifier = modifier.fillMaxSize(),
  ) {
    when (val child = it.instance) {
      is RootRoutineComponent.ChildStack.RoutineMainStack -> {
        RoutineMainUi(showSearchBar = showSearchBar, component = child.component)
      }
    }
  }

  val dialogSlotErrorRemoveRoutineDialog by component.errorDialogRemoveRoutineScreen.subscribeAsState()
  val dialogSlotErrorDialog by component.errorDialogScreen.subscribeAsState()
  val dialogSlotUpdateRoutine by component.updateRoutineDialogScreen.subscribeAsState()

  dialogSlotErrorRemoveRoutineDialog.child?.let { dialogSlot ->
    dialogSlot.instance.also { dialogComponent ->
      ErrorDialogRemoveRoutineUi(component = dialogComponent)
    }
  }

  dialogSlotErrorDialog.child?.let { dialogSlot ->
    dialogSlot.instance.also { dialogComponent ->
      ErrorDialogUi(component = dialogComponent)
    }
  }

  dialogSlotUpdateRoutine.child?.let { dialogSlot ->
    dialogSlot.instance.also { dialogComponent ->
      UpdateRoutineDialog(
        component = dialogComponent,
      )
    }
  }
}


