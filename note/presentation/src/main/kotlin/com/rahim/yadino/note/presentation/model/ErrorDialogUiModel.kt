package com.rahim.yadino.note.presentation.model

import kotlinx.serialization.Serializable

@Serializable
data class ErrorDialogUiModel(val title: String, val submitTextButton: String, val noteUiModel: NoteUiModel)
