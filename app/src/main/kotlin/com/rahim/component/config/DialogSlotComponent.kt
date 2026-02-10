package com.rahim.component.config

import com.rahim.yadino.note.presentation.model.NoteUiModel
import kotlinx.serialization.Serializable


@Serializable
data object AddRoutineDialogRoutineScreen : ConfigChildComponent

@Serializable
data class UpdateRoutineDialogRoutineScreen(val updateRoutine: com.rahim.yadino.routine.presentation.model.RoutineUiModel) : ConfigChildComponent

@Serializable
data class ErrorDialogRoutine(val errorDialogUiModel: com.rahim.yadino.routine.presentation.model.ErrorDialogUiModel) : ConfigChildComponent
