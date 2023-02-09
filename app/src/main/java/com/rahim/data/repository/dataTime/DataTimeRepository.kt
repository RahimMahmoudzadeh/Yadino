package com.rahim.data.repository.dataTime

import com.rahim.data.modle.data.TimeData

interface DataTimeRepository {
    suspend fun getTodayNumber(): Int

    suspend fun getMonthLength(): String

    suspend fun addAllTime(timesData: List<TimeData>)

    suspend fun addTime(timeData: TimeData)

    suspend fun getTime(): List<TimeData>

    suspend fun getDayName(): String

    suspend fun getYerNumber(): Int

    suspend fun getMonthName(): String

    suspend fun calculationDayNameBeforeCurrentDay()

    suspend fun calculationDayNameAfterCurrentDay()

    suspend fun updateTime(timeData: TimeData)
}