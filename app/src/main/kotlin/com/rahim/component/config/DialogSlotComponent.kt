package com.rahim.component.config

import com.rahim.yadino.home.presentation.model.ErrorDialogUiModel
import com.rahim.yadino.home.presentation.model.RoutineUiModel
import com.rahim.yadino.note.presentation.model.NoteUiModel
import kotlinx.serialization.Serializable

@Serializable
data object AddRoutineDialogHomeScreen : ConfigChildComponent

@Serializable
data class UpdateRoutineDialogHomeScreen(val updateRoutine: RoutineUiModel) : ConfigChildComponent

@Serializable
data object AddRoutineDialogRoutineScreen : ConfigChildComponent

@Serializable
data class UpdateRoutineDialogRoutineScreen(val updateRoutine: com.rahim.yadino.routine.presentation.model.RoutineUiModel) : ConfigChildComponent

@Serializable
data class AddNoteDialog(val updateNote: NoteUiModel? = null) : ConfigChildComponent

@Serializable
data class ErrorDialogHome(val errorDialogUiModel: ErrorDialogUiModel) : ConfigChildComponent

@Serializable
data class ErrorDialogRoutine(val errorDialogUiModel: com.rahim.yadino.routine.presentation.model.ErrorDialogUiModel) : ConfigChildComponent

@Serializable
data class ErrorDialogNote(val errorDialogUiModel: com.rahim.yadino.note.presentation.model.ErrorDialogUiModel) : ConfigChildComponent
