package com.rahim.data.repository.dataTime

import com.rahim.data.modle.data.TimeDate
import com.rahim.data.modle.data.TimeDataMonthAndYear
import kotlinx.coroutines.flow.Flow

interface DataTimeRepository {
    suspend fun addTime()
    suspend fun calculateToday()
    fun getTimes(): Flow<List<TimeDate>>
    fun getTimesMonth(yerNumber: Int, monthNumber: Int): Flow<List<TimeDate>>
    fun getCurrentNameDay(date:String,format:String):String
}