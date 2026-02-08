package com.rahim.yadino.home.presentation.ui.root.component

import com.rahim.yadino.home.presentation.model.ErrorDialogUiModel
import com.rahim.yadino.home.presentation.model.RoutineUiModel
import kotlinx.serialization.Serializable

sealed interface DialogSlotHomeComponent {
  @Serializable
  data object AddRoutineDialog : DialogSlotHomeComponent

  @Serializable
  data class UpdateRoutineDialog(val updateRoutine: RoutineUiModel) : DialogSlotHomeComponent

  @Serializable
  data class ErrorDialogRemoveRoutine(val errorDialogUiModel: ErrorDialogUiModel) : DialogSlotHomeComponent

}
