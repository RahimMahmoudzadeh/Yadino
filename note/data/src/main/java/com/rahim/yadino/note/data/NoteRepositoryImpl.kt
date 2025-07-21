package com.rahim.yadino.note.data

import com.rahim.yadino.db.dao.note.dao.NoteDao
import com.rahim.yadino.db.dao.note.model.NoteEntity
import com.rahim.yadino.note.data.mapper.toNoteEntity
import com.rahim.yadino.note.data.mapper.toNoteModel
import com.rahim.yadino.note.domain.NoteRepository
import com.rahim.yadino.note.domain.model.NoteModel
import com.rahim.yadino.sharedPreferences.repo.SharedPreferencesRepository
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
        NoteEntity(
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
      noteDao.insertNote(note)
    }
  }

  override suspend fun addNote(noteModal: NoteModel) {
    sharedPreferencesRepository.setShowSampleNote()
    noteDao.insertNote(noteModal.toNoteEntity())
  }

  override suspend fun updateNote(noteModal: NoteModel) {
    sharedPreferencesRepository.setShowSampleNote()
    noteDao.update(noteModal.toNoteEntity())
  }

  override suspend fun deleteNote(noteModal: NoteModel) {
    sharedPreferencesRepository.setShowSampleNote()
    noteDao.delete(noteModal.toNoteEntity())
  }

  override fun getNotes(): Flow<List<NoteModel>> = noteDao.getNotes().map { it.map { it.toNoteModel() } }

  override fun searchNote(name: String): Flow<List<NoteModel>> = noteDao.searchRoutine(name).map { it.map { it.toNoteModel() } }
}
