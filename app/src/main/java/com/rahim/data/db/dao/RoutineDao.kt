package com.rahim.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.rahim.data.modle.Rotin.Routine
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutineDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRoutine(routine: Routine): Long

    @Query("SELECT * FROM tbl_routine WHERE id =:id")
    suspend fun getRoutine(id: Int): Routine

    @Query("SELECT * FROM tbl_routine WHERE monthNumber =:monthNumber AND dayNumber =:dayNumber AND yerNumber =:yerNumber")
    fun getRoutines(monthNumber: Int, dayNumber: Int, yerNumber: Int): Flow<List<Routine>>

    @Query("SELECT * FROM tbl_routine")
    suspend fun getRoutines(): List<Routine>

    @Query("SELECT * FROM tbl_routine WHERE explanation=:routineExplanation AND dayName=:routineDayName AND timeInMillisecond=:routineTimeMilSecond AND name=:routineName AND dayNumber=:routineDayNumber AND monthNumber=:routineMonthNumber AND yerNumber=:routineYearNumber LIMIT 1")
    suspend fun checkEqualRoutine(
        routineName: String,
        routineExplanation: String,
        routineDayName: String,
        routineDayNumber: Int,
        routineYearNumber: Int,
        routineMonthNumber: Int,
        routineTimeMilSecond: Long
    ): Routine?
    @Query("DELETE FROM tbl_routine WHERE dayNumber=:dayNumber AND monthNumber=:monthNumber AND yerNumber=:yerNumber")
    suspend fun removeAllRoutine(monthNumber: Int?, dayNumber: Int?, yerNumber: Int?)

    @Delete
    suspend fun removeRoutine(routine: Routine): Int

    @Update
    suspend fun updateRoutine(routine: Routine)

    @Query("Update tbl_routine SET isChecked=1 WHERE idAlarm=:id")
    suspend fun updateCheckedByAlarmId(id: Long)

    @Query("SELECT * FROM tbl_routine WHERE monthNumber=:monthNumber AND dayNumber=:dayNumber AND name LIKE '%'||:nameRoutine|| '%'")
    fun searchRoutine(
        nameRoutine: String,
        monthNumber: Int?,
        dayNumber: Int?
    ): Flow<List<Routine>>

    @Query("SELECT * FROM tbl_routine WHERE isSample=1")
    suspend fun getSampleRoutines(): List<Routine>

    @Query("DELETE FROM tbl_routine WHERE isSample=1")
    suspend fun removeSampleRoutine()

    @Query("SELECT idAlarm FROM tbl_routine WHERE isChecked=0")
    suspend fun getIdAlarms(): List<Long>
}