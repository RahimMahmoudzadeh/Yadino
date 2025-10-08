package com.rahim.yadino.db.routine.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.rahim.yadino.db.routine.model.RoutineEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutineDao {
  @Upsert
  suspend fun addRoutine(routineEntity: RoutineEntity): Long

  @Query("SELECT * FROM tbl_routine WHERE id =:id")
  suspend fun getRoutine(id: Int): RoutineEntity

  @Query("SELECT * FROM tbl_routine WHERE  yearNumber =:yearNumber AND monthNumber =:monthNumber AND dayNumber =:dayNumber")
  fun getRoutinesByDate(monthNumber: Int, dayNumber: Int, yearNumber: Int): Flow<List<RoutineEntity>>

  @Query("SELECT * FROM tbl_routine")
  suspend fun getRoutinesByDate(): List<RoutineEntity>

  @Query("UPDATE tbl_routine SET isChecked=1 WHERE isChecked=0 AND timeInMillisecond<:currentTime AND isSample=0")
  suspend fun updateRoutinesPastTime(currentTime: Long)

  @Query("SELECT * FROM tbl_routine WHERE explanation=:routineExplanation AND dayName=:routineDayName AND timeInMillisecond=:routineTimeMilSecond AND name=:routineName AND dayNumber=:routineDayNumber AND monthNumber=:routineMonthNumber AND yearNumber=:routineYearNumber LIMIT 1")
  suspend fun checkEqualRoutine(routineName: String, routineExplanation: String, routineDayName: String, routineDayNumber: Int, routineYearNumber: Int, routineMonthNumber: Int, routineTimeMilSecond: Long): RoutineEntity?

  @Query("DELETE FROM tbl_routine WHERE dayNumber=:dayNumber AND monthNumber=:monthNumber AND yearNumber=:yearNumber")
  suspend fun removeAllRoutine(monthNumber: Int?, dayNumber: Int?, yearNumber: Int?)

  @Delete
  suspend fun removeRoutine(routineEntity: RoutineEntity): Int

  @Query("Update tbl_routine SET isChecked=1 WHERE idAlarm=:id")
  suspend fun updateCheckedByAlarmId(id: Long)

  @Query("SELECT * FROM tbl_routine WHERE yearNumber =:yearNumber AND monthNumber=:monthNumber AND dayNumber=:dayNumber AND name LIKE '%'||:nameRoutine|| '%'")
  fun searchRoutine(nameRoutine: String, monthNumber: Int?, dayNumber: Int?, yearNumber: Int?): Flow<List<RoutineEntity>>

  @Query("SELECT * FROM tbl_routine WHERE isSample=1")
  suspend fun getSampleRoutines(): List<RoutineEntity>

  @Query("DELETE FROM tbl_routine WHERE isSample=1")
  suspend fun removeSampleRoutine()

  @Query("SELECT idAlarm FROM tbl_routine WHERE isChecked=0")
  suspend fun getIdAlarms(): List<Long>

  @Query("SELECT EXISTS(SELECT 1 FROM tbl_routine WHERE isChecked = 0)")
  fun haveAlarm(): Flow<Boolean>
}
