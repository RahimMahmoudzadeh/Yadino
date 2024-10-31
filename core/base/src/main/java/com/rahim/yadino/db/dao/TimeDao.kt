package com.rahim.yadino.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.rahim.yadino.model.TimeDate
import kotlinx.coroutines.flow.Flow

@Dao
interface TimeDao {
    @Insert(onConflict = REPLACE)
    suspend fun insertAllTime(timesDate: List<TimeDate>)

    @Insert(onConflict = REPLACE)
    suspend fun insertTime(timesDate: TimeDate)

    @Query("SELECT * FROM tbl_timeDate Where isToday = 1 ")
    suspend fun getToday(): TimeDate?

    @Query("SELECT * FROM tbl_timeDate ORDER BY yerNumber ASC , monthNumber ASC , dayNumber ASC")
    suspend fun getAllTime(): List<TimeDate>

    @Query("SELECT * FROM tbl_timeDate ORDER BY yerNumber ASC , monthNumber ASC , dayNumber ASC")
    fun getAllTimeFlow(): Flow<List<TimeDate>>
    @Query("SELECT * FROM tbl_timeDate WHERE monthNumber=:monthNumber And yerNumber=:yerNumber")
    fun getIsSpecificMonthFromYer(monthNumber: Int, yerNumber: Int?): Boolean
    @Query("SELECT * FROM tbl_timeDate WHERE monthNumber=:monthNumber And yerNumber=:yerNumber")
    fun getSpecificMonthFromYer(monthNumber: Int, yerNumber: Int?): Flow<List<TimeDate>>
    @Update
    suspend fun updateTimeData(timeDate: TimeDate)
    @Query("UPDATE tbl_timeDate SET isToday=1 WHERE dayNumber=:currentDay AND yerNumber=:currentYer AND monthNumber=:currentMonth")
    suspend fun updateDayToToday(currentDay:Int, currentYer:Int, currentMonth:Int)
    @Query("UPDATE tbl_timeDate SET isToday=0 WHERE dayNumber=:currentDay AND yerNumber=:currentYer AND monthNumber=:currentMonth")
    suspend fun updateDayToNotToday(currentDay:Int, currentYer:Int, currentMonth:Int)
    @Query("DELETE FROM tbl_timeDate")
    suspend fun deleteAllTimes()
}
