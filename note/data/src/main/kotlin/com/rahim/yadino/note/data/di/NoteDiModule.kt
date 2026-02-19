package com.rahim.yadino.note.data.di

import com.rahim.yadino.note.data.NoteRepositoryImpl
import com.rahim.yadino.note.domain.NoteRepository
import com.rahim.yadino.note.domain.useCase.AddNoteUseCase
import com.rahim.yadino.note.domain.useCase.AddSampleNoteUseCase
import com.rahim.yadino.note.domain.useCase.DeleteNoteUseCase
import com.rahim.yadino.note.domain.useCase.GetNotesUseCase
import com.rahim.yadino.note.domain.useCase.SearchNoteUseCase
import com.rahim.yadino.note.domain.useCase.UpdateNoteUseCase
import org.koin.dsl.module

val NoteDiModule = module {
  single<NoteRepository> { NoteRepositoryImpl(noteDao = get(), sharedPreferencesRepository = get()) }
  single { AddNoteUseCase(noteRepository = get()) }
  single { UpdateNoteUseCase(noteRepository = get()) }
  single { DeleteNoteUseCase(noteRepository = get()) }
  single { GetNotesUseCase(noteRepository = get()) }
  single { SearchNoteUseCase(noteRepository = get()) }
  single { AddSampleNoteUseCase(noteRepository = get()) }
}
