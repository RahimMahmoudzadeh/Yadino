package com.rahim.data.repository.dataTime

import com.rahim.data.modle.data.TimeData
import kotlinx.coroutines.flow.Flow

interface DataTimeRepository {
    suspend fun addTime()
    suspend fun calculateToday()
    fun getTimes(): Flow<List<TimeData>>
}