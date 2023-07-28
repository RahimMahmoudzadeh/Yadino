package com.rahim.data.repository.note

import com.rahim.data.db.database.AppDatabase
import com.rahim.data.modle.Rotin.Routine
import com.rahim.data.modle.note.NoteModel
import com.rahim.data.sharedPreferences.SharedPreferencesCustom
import kotlinx.coroutines.flow.Flow
import saman.zamani.persiandate.PersianDate
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    val appDatabase: AppDatabase,
    val sharedPreferencesCustom: SharedPreferencesCustom
) : NoteRepository {
    private val persianData = PersianDate()
    private val currentTimeDay = persianData.shDay
    private val currentTimeMonth = persianData.shMonth
    private val currentTimeYer = persianData.shYear
    override suspend fun addSampleNote() {
        val sampleNotes = appDatabase.noteDao()
            .getSampleNote()

        if (sharedPreferencesCustom.isSampleNote()) {
            appDatabase.noteDao().removeSampleNote()
            return
        }

        if (sampleNotes.isNotEmpty())
            return

        (0..1).forEach {
            val note = if (it == 0) {
                NoteModel(
                    name = "تست1",
                    description = "من یک یادداشت تستی هستم لطفا من را به راست بکشید",
                    state = 2,
                    dayName = currentTimeDay.toString(),
                    dayNumber = currentTimeDay,
                    yerNumber = currentTimeYer,
                    monthNumber = currentTimeMonth,
                    isSample = true
                )
            } else {
                NoteModel(
                    name = "تست2",
                    description = "من یک یادداشت تستی هستم لطفا من را به چپ بکشید",
                    state = 2,
                    dayName = currentTimeDay.toString(),
                    dayNumber = currentTimeDay,
                    yerNumber = currentTimeYer,
                    monthNumber = currentTimeMonth,
                    isSample = true
                )
            }
            addNote(note)
        }
    }

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