package com.rahim.yadino.dateTime

import com.rahim.yadino.base.db.model.TimeDate
import kotlinx.coroutines.flow.Flow

interface DateTimeRepository {
    suspend fun addTime()
    suspend fun calculateToday()
    fun getTimes(): Flow<List<TimeDate>>
    fun getTimesMonth(yerNumber: Int, monthNumber: Int): Flow<List<TimeDate>>
    fun getCurrentNameDay(date:String,format:String):String
}