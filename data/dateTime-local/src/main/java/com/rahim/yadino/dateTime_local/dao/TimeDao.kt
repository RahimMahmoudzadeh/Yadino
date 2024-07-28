package com.rahim.yadino.dateTime_local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.rahim.yadino.dateTime_local.dto.LocalTimeDateDto
import kotlinx.coroutines.flow.Flow

@Dao
interface TimeDao {
    @Insert(onConflict = REPLACE)
    suspend fun insertAllTime(timesDate: List<LocalTimeDateDto>)

    @Insert(onConflict = REPLACE)
    suspend fun insertTime(timesDate: LocalTimeDateDto)

    @Query("SELECT * FROM tbl_timeDate Where isToday = 1 ")
    suspend fun getToday(): LocalTimeDateDto?

    @Query("SELECT * FROM tbl_timeDate ORDER BY yerNumber ASC , monthNumber ASC , dayNumber ASC")
    suspend fun getAllTime(): List<LocalTimeDateDto>

    @Query("SELECT * FROM tbl_timeDate ORDER BY yerNumber ASC , monthNumber ASC , dayNumber ASC")
    fun getAllTimeFlow(): Flow<List<LocalTimeDateDto>>
    @Query("SELECT * FROM tbl_timeDate WHERE monthNumber=:monthNumber And yerNumber=:yerNumber")
    fun getIsSpecificMonthFromYer(monthNumber: Int, yerNumber: Int?): Boolean
    @Query("SELECT * FROM tbl_timeDate WHERE monthNumber=:monthNumber And yerNumber=:yerNumber")
    fun getSpecificMonthFromYer(monthNumber: Int, yerNumber: Int?): Flow<List<LocalTimeDateDto>>
    @Update
    suspend fun updateTimeData(timeDate: LocalTimeDateDto)
    @Query("UPDATE tbl_timeDate SET isToday=1 WHERE dayNumber=:currentDay AND yerNumber=:currentYer AND monthNumber=:currentMonth")
    suspend fun updateDayToToday(currentDay:Int, currentYer:Int, currentMonth:Int)
    @Query("UPDATE tbl_timeDate SET isToday=0 WHERE dayNumber=:currentDay AND yerNumber=:currentYer AND monthNumber=:currentMonth")
    suspend fun updateDayToNotToday(currentDay:Int, currentYer:Int, currentMonth:Int)
    @Query("DELETE FROM tbl_timeDate")
    suspend fun deleteAllTimes()
}