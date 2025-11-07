package com.rahim.yadino.note.presentation.di

import com.rahim.yadino.note.presentation.component.NoteComponentImpl
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val NotePresentationDiModule = module {
  viewModel { NoteComponentImpl(getNotesUseCase = get(), addNoteUseCase = get(), deleteNoteUseCase = get(), updateNoteUseCase = get(), searchNoteUseCase = get()) }
}
