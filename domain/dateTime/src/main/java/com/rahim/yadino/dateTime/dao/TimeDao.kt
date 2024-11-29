package com.rahim.yadino.dateTime.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.rahim.yadino.dateTime.model.TimeDate
import kotlinx.coroutines.flow.Flow

@Dao
interface TimeDao {
  @Insert(onConflict = REPLACE)
  suspend fun insertAllTime(timesDate: List<TimeDate>)

  @Insert(onConflict = REPLACE)
  suspend fun insertTime(timesDate: TimeDate)

  @Query("SELECT * FROM tbl_timeDate Where isToday = 1 ")
  suspend fun getToday(): TimeDate?

  @Query("SELECT * FROM tbl_timeDate ORDER BY yearNumber ASC , monthNumber ASC , dayNumber ASC")
  suspend fun getAllTime(): List<TimeDate>

  @Query("SELECT * FROM tbl_timeDate ORDER BY yearNumber ASC , monthNumber ASC , dayNumber ASC")
  fun getAllTimeFlow(): Flow<List<TimeDate>>

  @Query("SELECT * FROM tbl_timeDate WHERE monthNumber=:monthNumber And yearNumber=:yearNumber")
  fun getIsSpecificMonthFromYear(monthNumber: Int, yearNumber: Int?): Boolean

  @Query("SELECT * FROM tbl_timeDate WHERE monthNumber=:monthNumber And yearNumber=:yearNumber")
  suspend fun getSpecificMonthFromYear(monthNumber: Int, yearNumber: Int?): List<TimeDate>

  @Update
  suspend fun updateTimeData(timeDate: TimeDate)

  @Query("UPDATE tbl_timeDate SET isToday=1 , isChecked=1 WHERE dayNumber=:currentDay AND yearNumber=:currentYear AND monthNumber=:currentMonth")
  suspend fun updateDayToToday(currentDay: Int, currentYear: Int, currentMonth: Int)

  @Query("UPDATE tbl_timeDate SET isToday=0 , isChecked=0 WHERE isToday=1 OR isChecked=1")
  suspend fun updateDayToNotToday()

  @Query("UPDATE tbl_timeDate SET isChecked=1 WHERE dayNumber=:currentDay AND yearNumber=:currentYear AND monthNumber=:currentMonth")
  suspend fun updateIsChecked(currentDay: Int, currentYear: Int, currentMonth: Int)

  @Query("UPDATE tbl_timeDate SET isChecked=0 WHERE isChecked=1")
  suspend fun updateNotIsChecked()

  @Query("DELETE FROM tbl_timeDate")
  suspend fun deleteAllTimes()
}
