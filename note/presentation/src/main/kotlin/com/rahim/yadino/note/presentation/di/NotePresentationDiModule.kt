package com.rahim.yadino.note.presentation.di

import com.rahim.yadino.note.presentation.NoteComponent
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val NotePresentationDiModule = module {
  viewModel { NoteComponent(getNotesUseCase = get(), addNoteUseCase = get(), deleteNoteUseCase = get(), updateNoteUseCase = get(), searchNoteUseCase = get()) }
}
