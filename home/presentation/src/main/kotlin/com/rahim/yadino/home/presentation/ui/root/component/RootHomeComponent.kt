package com.rahim.yadino.home.presentation.ui.root.component

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import com.rahim.yadino.home.presentation.model.ErrorDialogRemoveUiModel
import com.rahim.yadino.home.presentation.model.RoutineUiModel
import com.rahim.yadino.home.presentation.ui.errorDialogRemoveRoutine.component.ErrorDialogRemoveRoutineComponent
import com.rahim.yadino.home.presentation.ui.main.component.MainHomeComponent
import com.rahim.yadino.home.presentation.ui.updateDialogRoutine.component.UpdateRoutineDialogComponent
import kotlinx.serialization.Serializable

interface RootHomeComponent {

  val stack: Value<com.arkivanov.decompose.router.stack.ChildStack<*, ChildStack>>

  val updateRoutineDialogScreen: Value<ChildSlot<DialogSlot.UpdateRoutineDialog, UpdateRoutineDialogComponent>>
  val errorDialogRemoveRoutineScreen: Value<ChildSlot<DialogSlot.ErrorDialogRemoveRoutine, ErrorDialogRemoveRoutineComponent>>

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
    data class UpdateRoutineDialog(val updateRoutine: RoutineUiModel) : DialogSlot

    @Serializable
    data class ErrorDialogRemoveRoutine(val errorDialogRemoveUiModel: ErrorDialogRemoveUiModel) : DialogSlot

//    @Serializable
//    data class ErrorDialog(val errorDialogUiModel: ErrorDialogUiModel) : DialogSlot
  }
}
