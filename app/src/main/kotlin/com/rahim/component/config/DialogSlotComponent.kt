package com.rahim.component.config

import com.rahim.yadino.home.presentation.model.RoutineUiModel
import kotlinx.serialization.Serializable

@Serializable
data class AddRoutineDialogHomeScreen(val routine: RoutineUiModel? = null) : ConfigChildComponent

@Serializable
data object AddRoutineDialogRoutineScreen : ConfigChildComponent
