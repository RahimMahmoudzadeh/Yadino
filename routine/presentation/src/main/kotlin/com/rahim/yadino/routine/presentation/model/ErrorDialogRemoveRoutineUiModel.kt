package com.rahim.yadino.routine.presentation.model

import kotlinx.serialization.Serializable

@Serializable
data class ErrorDialogRemoveRoutineUiModel(val title: String, val submitTextButton: String, val routineUiModel: RoutineUiModel)
