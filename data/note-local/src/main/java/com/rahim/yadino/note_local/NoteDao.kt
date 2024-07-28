package com.rahim.yadino.note_local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.rahim.yadino.note_local.dto.LocalNoteDto
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(noteModel: LocalNoteDto)

    @Query("SELECT * FROM tbl_note ORDER BY timeInMileSecond ASC")
    fun getNotes(): Flow<List<LocalNoteDto>>

    @Update
    suspend fun update(noteModel:LocalNoteDto)
    @Delete
    suspend fun delete(noteModel: LocalNoteDto)
    @Query("SELECT * FROM tbl_note WHERE name LIKE '%'||:searchName||'%' ORDER BY timeInMileSecond ASC")
    fun searchRoutine(
        searchName:String
    ): Flow<List<LocalNoteDto>>
    @Query("SELECT * FROM tbl_note WHERE isSample=1")
    suspend fun getSampleNote():List<LocalNoteDto>

    @Query("DELETE FROM tbl_note WHERE isSample=1")
    suspend fun removeSampleNote()
}