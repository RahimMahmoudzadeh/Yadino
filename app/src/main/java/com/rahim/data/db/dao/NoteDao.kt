package com.rahim.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rahim.data.modle.note.NoteModel
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(noteModel: NoteModel)

    @Query("SELECT * FROM tbl_note")
    fun getNotes(): Flow<List<NoteModel>>
}