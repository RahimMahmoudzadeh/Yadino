package com.rahim.yadino.db.note.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.rahim.yadino.db.note.model.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
  @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
  suspend fun insertNote(noteEntity: NoteEntity)

  @Query("SELECT * FROM tbl_note ORDER BY timeInMileSecond ASC")
  fun getNotes(): Flow<List<NoteEntity>>

  @Update
  suspend fun update(noteEntity: NoteEntity)

  @Delete
  suspend fun delete(noteEntity: NoteEntity)

  @Query("SELECT * FROM tbl_note WHERE name LIKE '%'||:searchName||'%' ORDER BY timeInMileSecond ASC")
  fun searchRoutine(searchName: String): Flow<List<NoteEntity>>

  @Query("SELECT * FROM tbl_note WHERE isSample=1")
  suspend fun getSampleNote(): List<NoteEntity>

  @Query("DELETE FROM tbl_note WHERE isSample=1")
  suspend fun removeSampleNote()
}
