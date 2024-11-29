package com.rahim.yadino.note_repository

import com.rahim.yadino.note.NoteRepository
import com.rahim.yadino.note.model.NoteModel
import com.rahim.yadino.note.dao.NoteDao
import com.rahim.yadino.sharedPreferences.SharedPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import saman.zamani.persiandate.PersianDate
import javax.inject.Inject

private const val SAMPLE_NOTE_RIGHT = "من یک یادداشت تستی هستم لطفا من را به راست بکشید"
private const val SAMPLE_NOTE_LEFT = "من یک یادداشت تستی هستم لطفا من را به چپ بکشید"

class NoteRepositoryImpl @Inject constructor(
  private val noteDao: NoteDao,
  private val sharedPreferencesRepository: SharedPreferencesRepository,
) : NoteRepository {
  private val persianData = PersianDate()
  private val currentTimeDay = persianData.shDay
  private val currentTimeMonth = persianData.shMonth
  private val currentTimeYear = persianData.shYear
  override suspend fun addSampleNote() {
    if (sharedPreferencesRepository.isShowSampleNote().first()) {
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
          yearNumber = currentTimeYear,
          monthNumber = currentTimeMonth,
          isSample = true,
          id = index,
        )
      addNote(note)
    }
  }

  override suspend fun addNote(noteModel: NoteModel) {
    sharedPreferencesRepository.setShowSampleNote()
    noteDao.insertNote(noteModel)
  }

  override suspend fun updateNote(noteModel: NoteModel) {
    sharedPreferencesRepository.setShowSampleNote()
    noteDao.update(noteModel)
  }

  override suspend fun deleteNote(noteModel: NoteModel) {
    sharedPreferencesRepository.setShowSampleNote()
    noteDao.delete(noteModel)
  }

  override fun getNotes(): Flow<List<NoteModel>> =
    noteDao.getNotes().map { it.map { it } }

  override fun searchNote(
    name: String,
  ): Flow<List<NoteModel>> = noteDao.searchRoutine(name).map { it.map { it } }

}
