package com.rahim.data.repository.dataTime

import com.rahim.yadino.routine.modle.data.TimeDate
import com.rahim.yadino.routine.modle.data.TimeDataMonthAndYear
import kotlinx.coroutines.flow.Flow

interface DataTimeRepository {
    suspend fun addTime()
    suspend fun calculateToday()
    fun getTimes(): Flow<List<com.rahim.yadino.routine.modle.data.TimeDate>>
    fun getTimesMonth(yerNumber: Int, monthNumber: Int): Flow<List<com.rahim.yadino.routine.modle.data.TimeDate>>
    fun getCurrentNameDay(date:String,format:String):String
}