package com.rahim.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.rahim.data.modle.Rotin.Routine
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutineDao {
    @Insert()
    suspend fun addRoutine(routine: Routine): Long

    @Query("SELECT * FROM tbl_routine WHERE id =:id")
    suspend fun getRoutine(id: Int): Routine

    @Query("SELECT * FROM tbl_routine WHERE monthNumber LIKE :monthNumber AND dayNumber LIKE :dayNumber AND yerNumber LIKE :yerNumber")
    fun getRoutines(monthNumber: Int, dayNumber: Int, yerNumber: Int): Flow<List<Routine>>

    @Query("DELETE FROM tbl_routine WHERE dayNumber=:dayNumber AND monthNumber=:monthNumber AND yerNumber=:yerNumber")
    suspend fun removeAllRoutine(monthNumber: Int?, dayNumber: Int?, yerNumber: Int?)

    @Delete
    suspend fun removeRoutine(routine: Routine): Int

    @Update
    suspend fun updateRoutine(routine: Routine)

    @Query("SELECT * FROM tbl_routine WHERE monthNumber=:monthNumber AND dayNumber=:dayNumber AND name LIKE '%'||:nameRoutine|| '%'")
    fun searchRoutine(
        nameRoutine: String,
        monthNumber: Int?,
        dayNumber: Int?
    ): Flow<List<Routine>>

    @Query("SELECT * FROM tbl_routine WHERE isSample=1 AND  monthNumber LIKE :monthNumber AND dayNumber LIKE :dayNumber AND yerNumber LIKE :yerNumber")
    suspend fun getSampleRoutine(monthNumber: Int, dayNumber: Int, yerNumber: Int):List<Routine>

    @Query("DELETE FROM tbl_routine WHERE isSample=1")
    suspend fun removeSampleRoutine()

    @Query("SELECT idAlarm FROM tbl_routine WHERE isChecked=0")
    fun getIdAlarms():Flow<List<Long>>
}