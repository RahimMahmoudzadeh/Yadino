package com.rahim.yadino.noteRepository.dao

import com.rahim.yadino.noteRepository.model.NoteEntity
import kotlinx.coroutines.flow.Flow

@androidx.room.Dao
interface NoteDao {
  @androidx.room.Insert(onConflict = androidx.room.OnConflictStrategy.Companion.REPLACE)
  suspend fun insertNote(noteEntity: NoteEntity)

  @androidx.room.Query("SELECT * FROM tbl_note ORDER BY timeInMileSecond ASC")
  fun getNotes(): Flow<List<NoteEntity>>

  @androidx.room.Update
  suspend fun update(noteEntity: NoteEntity)

  @androidx.room.Delete
  suspend fun delete(noteEntity: NoteEntity)

  @androidx.room.Query("SELECT * FROM tbl_note WHERE name LIKE '%'||:searchName||'%' ORDER BY timeInMileSecond ASC")
  fun searchRoutine(searchName: String): Flow<List<NoteEntity>>

  @androidx.room.Query("SELECT * FROM tbl_note WHERE isSample=1")
  suspend fun getSampleNote(): List<NoteEntity>

  @androidx.room.Query("DELETE FROM tbl_note WHERE isSample=1")
  suspend fun removeSampleNote()
}
