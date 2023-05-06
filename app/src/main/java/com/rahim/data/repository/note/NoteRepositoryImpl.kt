package com.rahim.data.repository.note

import com.rahim.data.db.database.AppDatabase
import com.rahim.data.modle.note.NoteModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(val appDatabase: AppDatabase) : NoteRepository {
    override suspend fun addNote(noteModel: NoteModel) {
        appDatabase.noteDao().insertNote(noteModel)
    }

    override suspend fun updateNote(noteModel: NoteModel) {
        appDatabase.noteDao().update(noteModel)
    }

    override suspend fun deleteNote(noteModel: NoteModel) {
        appDatabase.noteDao().delete(noteModel)
    }

    override fun getNotes(): Flow<List<NoteModel>> = appDatabase.noteDao().getNotes()
}