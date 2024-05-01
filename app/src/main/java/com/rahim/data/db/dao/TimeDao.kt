package com.rahim.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.rahim.data.modle.data.TimeData
import kotlinx.coroutines.flow.Flow

@Dao
interface TimeDao {
    @Insert(onConflict = REPLACE)
    suspend fun insertAllTime(timesData: List<TimeData>)

    @Insert(onConflict = REPLACE)
    suspend fun insertTime(timesData: TimeData)

    @Query("SELECT * FROM tbl_timeData Where isToday = 1 ")
    suspend fun getToday(): TimeData?

    @Query("SELECT * FROM tbl_timeData")
    suspend fun getAllTime(): List<TimeData>
    @Query("SELECT * FROM tbl_timeData")
    fun getAllTimeFlow(): Flow<List<TimeData>>
    @Query("SELECT * FROM tbl_timeData WHERE monthNumber=:monthNumber And yerNumber=:yerNumber")
    fun getIsSpecificMonthFromYer(monthNumber: Int, yerNumber: Int?): Boolean
    @Query("SELECT * FROM tbl_timeData WHERE monthNumber=:monthNumber And yerNumber=:yerNumber")
    fun getSpecificMonthFromYer(monthNumber: Int, yerNumber: Int?): Flow<List<TimeData>>
    @Update
    suspend fun updateTimeData(timeData: TimeData)
    @Query("UPDATE tbl_timeData SET isToday=1 WHERE dayNumber=:currentDay AND yerNumber=:currentYer AND monthNumber=:currentMonth")
    suspend fun updateDayToToday(currentDay:Int, currentYer:Int, currentMonth:Int)
    @Query("UPDATE tbl_timeData SET isToday=0 WHERE dayNumber=:currentDay AND yerNumber=:currentYer AND monthNumber=:currentMonth")
    suspend fun updateDayToNotToday(currentDay:Int, currentYer:Int, currentMonth:Int)
    @Query("DELETE FROM tbl_timeData")
    suspend fun deleteAllTimes()
}