package com.rahim.yadino.note_repository.di

import com.rahim.yadino.note.NoteRepository
import com.rahim.yadino.note_repository.NoteRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface NoteRepositoryModule {
    @Binds
    fun bindsNoteRepository(noteRepositoryImpl: NoteRepositoryImpl): NoteRepository
}