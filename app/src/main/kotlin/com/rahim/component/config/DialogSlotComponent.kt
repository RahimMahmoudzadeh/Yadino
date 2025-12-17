package com.rahim.component.config

import com.rahim.yadino.home.presentation.model.RoutineUiModel
import com.rahim.yadino.note.presentation.model.NoteUiModel
import kotlinx.serialization.Serializable

@Serializable
data class AddRoutineDialogHomeScreen(val routine: RoutineUiModel? = null) : ConfigChildComponent

@Serializable
data class AddRoutineDialogRoutineScreen(val updateRoutine: com.yadino.routine.presentation.model.RoutineUiModel? = null) : ConfigChildComponent

@Serializable
data class AddNoteDialog(val updateNote: NoteUiModel? = null) : ConfigChildComponent

@Serializable
data class ErrorDialog(val itemId: String) : ConfigChildComponent
