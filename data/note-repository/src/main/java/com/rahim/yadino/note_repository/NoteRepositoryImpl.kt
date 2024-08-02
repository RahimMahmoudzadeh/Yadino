package com.rahim.yadino.note_repository

import com.rahim.yadino.base.sharedPreferences.SharedPreferencesCustom
import com.rahim.yadino.note.NoteRepository
import com.rahim.yadino.note.model.NoteModel
import com.rahim.yadino.note_repository.mapper.toLocalNoteDto
import com.rahim.yadino.note_repository.mapper.toNote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import saman.zamani.persiandate.PersianDate
import javax.inject.Inject

private const val SAMPLE_NOTE_RIGHT = "من یک یادداشت تستی هستم لطفا من را به راست بکشید"
private const val SAMPLE_NOTE_LEFT = "من یک یادداشت تستی هستم لطفا من را به چپ بکشید"

class NoteRepositoryImpl @Inject constructor(
    private val noteDao: com.rahim.yadino.note_local.NoteDao,
    private val sharedPreferencesCustom: SharedPreferencesCustom
) : NoteRepository {
    private val persianData = PersianDate()
    private val currentTimeDay = persianData.shDay
    private val currentTimeMonth = persianData.shMonth
    private val currentTimeYer = persianData.shYear
    override suspend fun addSampleNote() {
        if (sharedPreferencesCustom.isSampleNote()) {
            noteDao.removeSampleNote()
            return
        }
        (0..1).forEachIndexed { index, it ->
            val note =
                NoteModel(
                    name = "تست${index.plus(1)}",
                    description = if (index == 1) SAMPLE_NOTE_RIGHT else SAMPLE_NOTE_LEFT,
                    state = 2,
                    dayName = currentTimeDay.toString(),
                    dayNumber = currentTimeDay,
                    yerNumber = currentTimeYer,
                    monthNumber = currentTimeMonth,
                    isSample = true,
                    id = index
                )
            addNote(note)
        }
    }

    override suspend fun addNote(noteModel: NoteModel) {
        noteDao.insertNote(noteModel.toLocalNoteDto())
    }

    override suspend fun updateNote(noteModel: NoteModel) {
        noteDao.update(noteModel.toLocalNoteDto())
    }

    override suspend fun deleteNote(noteModel: NoteModel) {
        noteDao.delete(noteModel.toLocalNoteDto())
    }

    override fun getNotes(): Flow<List<NoteModel>> =
        noteDao.getNotes().map { it.map { it.toNote() } }

    override fun searchNote(
        name: String
    ): Flow<List<NoteModel>> = noteDao.searchRoutine(name).map { it.map { it.toNote() } }

}