package com.rahim.yadino.home.presentation.ui.root.component

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import com.rahim.yadino.home.presentation.model.ErrorDialogRemoveUiModel
import com.rahim.yadino.home.presentation.model.ErrorDialogUiModel
import com.rahim.yadino.home.presentation.model.RoutineUiModel
import com.rahim.yadino.home.presentation.ui.addDialogRoutine.component.AddRoutineDialogComponent
import com.rahim.yadino.home.presentation.ui.errorDialog.component.ErrorDialogComponent
import com.rahim.yadino.home.presentation.ui.errorDialogRemoveRoutine.component.ErrorDialogRemoveRoutineComponent
import com.rahim.yadino.home.presentation.ui.main.component.MainHomeComponent
import com.rahim.yadino.home.presentation.ui.updateDialogRoutine.component.UpdateRoutineDialogComponent
import kotlinx.serialization.Serializable

interface RootHomeComponent {

  val stack: Value<com.arkivanov.decompose.router.stack.ChildStack<*, ChildStack>>

  val addRoutineDialogScreen: Value<ChildSlot<DialogSlot.AddRoutineDialog, AddRoutineDialogComponent>>
  val updateRoutineDialogScreen: Value<ChildSlot<DialogSlot.UpdateRoutineDialog, UpdateRoutineDialogComponent>>
  val errorDialogRemoveRoutineScreen: Value<ChildSlot<DialogSlot.ErrorDialogRemoveRoutine, ErrorDialogRemoveRoutineComponent>>
  val errorDialogScreen: Value<ChildSlot<DialogSlot.ErrorDialog, ErrorDialogComponent>>

  sealed interface ChildStack {
    class HomeMainStack(val component: MainHomeComponent) : ChildStack
  }

  @Serializable
  sealed interface ChildConfig {
    @Serializable
    data object HomeMain : ChildConfig
  }

  sealed interface DialogSlot {
    @Serializable
    data object AddRoutineDialog : DialogSlot

    @Serializable
    data class UpdateRoutineDialog(val updateRoutine: RoutineUiModel) : DialogSlot

    @Serializable
    data class ErrorDialogRemoveRoutine(val errorDialogRemoveUiModel: ErrorDialogRemoveUiModel) : DialogSlot

    @Serializable
    data class ErrorDialog(val errorDialogUiModel: ErrorDialogUiModel) : DialogSlot
  }
}
