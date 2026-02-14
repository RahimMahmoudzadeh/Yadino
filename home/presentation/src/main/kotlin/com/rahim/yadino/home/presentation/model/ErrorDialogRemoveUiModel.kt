package com.rahim.yadino.home.presentation.model

import kotlinx.serialization.Serializable

@Serializable
data class ErrorDialogRemoveUiModel(val title: String, val submitTextButton: String, val routineUiModel: RoutineUiModel)
