package com.rahim.yadino.routine.presentation.ui.root.component

import com.rahim.yadino.routine.presentation.model.ErrorDialogRemoveRoutineUiModel
import com.rahim.yadino.routine.presentation.model.ErrorDialogUiModel
import com.rahim.yadino.routine.presentation.model.RoutineUiModel
import kotlinx.serialization.Serializable

@Serializable
sealed interface DialogSlotComponent{
  @Serializable
  data object AddRoutineDialog : DialogSlotComponent

  @Serializable
  data class UpdateRoutineDialog(val updateRoutine: RoutineUiModel) : DialogSlotComponent

  @Serializable
  data class ErrorDialogRemoveRoutine(val errorDialogRemoveRoutineUiModel: ErrorDialogRemoveRoutineUiModel) : DialogSlotComponent

  @Serializable
  data class ErrorDialog(val errorDialogUiModel: ErrorDialogUiModel) : DialogSlotComponent
}
