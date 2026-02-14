package com.rahim.yadino.home.presentation.ui.root

import android.Manifest
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.rahim.yadino.base.use
import com.rahim.yadino.designsystem.component.requestNotificationPermission
import com.rahim.yadino.designsystem.utils.theme.CornflowerBlueLight
import com.rahim.yadino.home.presentation.model.ErrorDialogUiModel
import com.rahim.yadino.home.presentation.ui.addDialogRoutine.AddRoutineDialog
import com.rahim.yadino.home.presentation.ui.errorDialog.ErrorDialogUi
import com.rahim.yadino.home.presentation.ui.errorDialogRemoveRoutine.ErrorDialogRemoveRoutineUi
import com.rahim.yadino.home.presentation.ui.main.HomeMainScreen
import com.rahim.yadino.home.presentation.ui.root.component.RootHomeComponent
import com.rahim.yadino.home.presentation.ui.updateDialogRoutine.UpdateRoutineDialog
import com.rahim.yadino.library.designsystem.R
import com.rahim.yadino.showToastShort
import com.rahim.yadino.toStringResource
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeRoot(
  modifier: Modifier = Modifier,
  clickSearch: Boolean,
  component: RootHomeComponent,
) {

  val addRoutineDialog = component.addRoutineDialogScreen.subscribeAsState().value.child
  val updateRoutineDialog = component.updateRoutineDialogScreen.subscribeAsState().value.child
  val errorDialogRemoveRoutine = component.errorDialogRemoveRoutineScreen.subscribeAsState().value.child
  val errorDialog = component.errorDialogScreen.subscribeAsState().value.child

  addRoutineDialog?.let { dialogSlot ->
    dialogSlot.instance.also { dialogComponent ->
      AddRoutineDialog(
        component = dialogComponent,
      )
    }
  }

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

  errorDialog?.let { dialogSlot ->
    dialogSlot.instance.also { dialogComponent ->
      ErrorDialogUi(component = dialogComponent)
    }
  }

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

