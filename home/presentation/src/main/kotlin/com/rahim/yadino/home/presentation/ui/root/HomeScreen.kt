package com.rahim.yadino.home.presentation.ui.root

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.rahim.yadino.home.presentation.ui.errorDialogRemoveRoutine.ErrorDialogRemoveRoutineUi
import com.rahim.yadino.home.presentation.ui.main.HomeMainScreen
import com.rahim.yadino.home.presentation.ui.root.component.RootHomeComponent
import com.rahim.yadino.home.presentation.ui.updateDialogRoutine.UpdateRoutineDialog

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeRoot(
  modifier: Modifier = Modifier,
  clickSearch: Boolean,
  component: RootHomeComponent,
) {

  val updateRoutineDialog = component.updateRoutineDialogScreen.subscribeAsState().value.child
  val errorDialogRemoveRoutine = component.errorDialogRemoveRoutineScreen.subscribeAsState().value.child
//  val errorDialog = component.errorDialogScreen.subscribeAsState().value.child

  updateRoutineDialog?.let { dialogSlot ->
    dialogSlot.instance.also { dialogComponent ->
      UpdateRoutineDialog(
        component = dialogComponent,
      )
    }
  }

  errorDialogRemoveRoutine?.let { dialogSlot ->
    dialogSlot.instance.also { dialogComponent ->
      ErrorDialogRemoveRoutineUi(component = dialogComponent)
    }
  }

//  errorDialog?.let { dialogSlot ->
//    dialogSlot.instance.also { dialogComponent ->
//      ErrorDialogUi(component = dialogComponent)
//    }
//  }

  Children(
    stack = component.stack,
    modifier = modifier.fillMaxSize(),
    animation = stackAnimation(fade()),
  ) {
    when (val child = it.instance) {
      is RootHomeComponent.ChildStack.HomeMainStack -> {
        HomeMainScreen(clickSearch = clickSearch, component = child.component)
      }
    }
  }
}

