package com.rahim.yadino.note_repository.di

import com.rahim.yadino.note.NoteRepository
import com.rahim.yadino.note_repository.NoteRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class NoteRepositoryModule {
    @Binds
    abstract fun provideDataTimeRepository(noteRepositoryImpl: NoteRepositoryImpl): NoteRepository
}