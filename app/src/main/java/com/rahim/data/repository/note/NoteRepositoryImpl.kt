package com.rahim.data.repository.note

import com.rahim.data.db.dao.NoteDao
import com.rahim.yadino.routine.modle.note.NoteModel
import com.rahim.yadino.base.sharedPreferences.SharedPreferencesCustom
import kotlinx.coroutines.flow.Flow
import saman.zamani.persiandate.PersianDate
import javax.inject.Inject

private const val SAMPLE_NOTE_RIGHT = "من یک یادداشت تستی هستم لطفا من را به راست بکشید"
private const val SAMPLE_NOTE_LEFT = "من یک یادداشت تستی هستم لطفا من را به چپ بکشید"

class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao,
    private val sharedPreferencesCustom: com.rahim.yadino.base.sharedPreferences.SharedPreferencesCustom
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
                com.rahim.yadino.routine.modle.note.NoteModel(
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

    override suspend fun addNote(noteModel: com.rahim.yadino.routine.modle.note.NoteModel) {
        noteDao.insertNote(noteModel)
    }

    override suspend fun updateNote(noteModel: com.rahim.yadino.routine.modle.note.NoteModel) {
        noteDao.update(noteModel)
    }

    override suspend fun deleteNote(noteModel: com.rahim.yadino.routine.modle.note.NoteModel) {
        noteDao.delete(noteModel)
    }

    override fun getNotes(): Flow<List<com.rahim.yadino.routine.modle.note.NoteModel>> = noteDao.getNotes()
    override fun searchNote(
        name: String
    ): Flow<List<com.rahim.yadino.routine.modle.note.NoteModel>> = noteDao.searchRoutine(name)

}