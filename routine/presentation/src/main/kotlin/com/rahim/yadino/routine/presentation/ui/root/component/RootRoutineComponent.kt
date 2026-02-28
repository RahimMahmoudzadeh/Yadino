package com.rahim.yadino.routine.presentation.ui.root.component

import androidx.compose.runtime.Immutable
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import com.rahim.yadino.base.LoadableData
import com.rahim.yadino.base.UnidirectionalComponent
import com.rahim.yadino.enums.message.error.ErrorMessage
import com.rahim.yadino.routine.presentation.model.ErrorDialogRemoveRoutineUiModel
import com.rahim.yadino.routine.presentation.model.ErrorDialogUiModel
import com.rahim.yadino.routine.presentation.model.IncreaseDecrease
import com.rahim.yadino.routine.presentation.model.RoutineUiModel
import com.rahim.yadino.routine.presentation.model.TimeDateUiModel
import com.rahim.yadino.routine.presentation.ui.addRoutineDialog.component.AddRoutineDialogComponent
import com.rahim.yadino.routine.presentation.ui.errorDialog.component.ErrorDialogComponent
import com.rahim.yadino.routine.presentation.ui.errorDialogRemoveRoutine.component.ErrorDialogRemoveRoutineComponent
import com.rahim.yadino.routine.presentation.ui.main.component.MainRoutineComponent
import com.rahim.yadino.routine.presentation.ui.updateDialogRoutine.component.UpdateRoutineDialogComponent
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable

interface RootRoutineComponent {

  val stack: Value<com.arkivanov.decompose.router.stack.ChildStack<*, ChildStack>>

  val updateRoutineDialogScreen: Value<ChildSlot<DialogSlot.UpdateRoutineDialog, UpdateRoutineDialogComponent>>
  val errorDialogRemoveRoutineScreen: Value<ChildSlot<DialogSlot.ErrorDialogRemoveRoutine, ErrorDialogRemoveRoutineComponent>>
  val errorDialogScreen: Value<ChildSlot<DialogSlot.ErrorDialog, ErrorDialogComponent>>

  sealed interface ChildStack {
    class RoutineMainStack(val component: MainRoutineComponent) : ChildStack
  }

  @Serializable
  sealed interface ChildConfig {
    @Serializable
    data object RoutineMain : ChildConfig
  }

  @Serializable
  sealed interface DialogSlot{
    @Serializable
    data class UpdateRoutineDialog(val updateRoutine: RoutineUiModel) : DialogSlot

    @Serializable
    data class ErrorDialogRemoveRoutine(val errorDialogRemoveRoutineUiModel: ErrorDialogRemoveRoutineUiModel) : DialogSlot

    @Serializable
    data class ErrorDialog(val errorDialogUiModel: ErrorDialogUiModel) : DialogSlot
  }

}
