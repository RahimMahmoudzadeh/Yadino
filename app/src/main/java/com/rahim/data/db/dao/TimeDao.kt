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

    @Query("SELECT * FROM tbl_timeData")
    suspend fun getAllTime(): List<TimeData>

    @Query("SELECT * FROM tbl_timeData")
    suspend fun getAllMonthDayNotFlow(): List<TimeData>

    @Query("SELECT * FROM tbl_timeData WHERE monthNumber=:monthNumber And yerNumber=:yerNumber")
    fun getIsSpecificMonthFromYer(monthNumber: String, yerNumber: Int?): Boolean

    @Query("SELECT * FROM tbl_timeData WHERE monthNumber=:monthNumber And yerNumber=:yerNumber")
    fun getSpecificMonthFromYer(monthNumber: String, yerNumber: Int?): Flow<List<TimeData>>

    @Update
    suspend fun updateTimeData(timeData: TimeData)

    @Query("DELETE FROM tbl_timeData")
    suspend fun deleteAllTimes()
}