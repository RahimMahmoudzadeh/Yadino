package com.rahim.data.repository.dataTime

import com.rahim.data.modle.data.TimeData
import kotlinx.coroutines.flow.Flow

interface DataTimeRepository {
    suspend fun addTime()
    suspend fun calculateToday()
    suspend fun getCurrentMonthDay(monthNumber: Int, yerNumber: Int?):Flow<List<TimeData>>
    fun getTimes(): Flow<List<TimeData>>
}