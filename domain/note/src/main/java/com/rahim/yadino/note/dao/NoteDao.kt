package com.rahim.yadino.note.dao

import com.rahim.yadino.note.model.NoteModel
import kotlinx.coroutines.flow.Flow

@androidx.room.Dao
interface NoteDao {
    @androidx.room.Insert(onConflict = androidx.room.OnConflictStrategy.Companion.REPLACE)
    suspend fun insertNote(noteModel: NoteModel)

    @androidx.room.Query("SELECT * FROM tbl_note ORDER BY timeInMileSecond ASC")
    fun getNotes(): Flow<List<NoteModel>>

    @androidx.room.Update
    suspend fun update(noteModel: NoteModel)
    @androidx.room.Delete
    suspend fun delete(noteModel: NoteModel)
    @androidx.room.Query("SELECT * FROM tbl_note WHERE name LIKE '%'||:searchName||'%' ORDER BY timeInMileSecond ASC")
    fun searchRoutine(
        searchName:String
    ): Flow<List<NoteModel>>
    @androidx.room.Query("SELECT * FROM tbl_note WHERE isSample=1")
    suspend fun getSampleNote():List<NoteModel>

    @androidx.room.Query("DELETE FROM tbl_note WHERE isSample=1")
    suspend fun removeSampleNote()
}
