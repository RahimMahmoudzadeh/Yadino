package com.rahim.yadino.core.timeDate.repo

import com.rahim.yadino.core.timeDate.model.TimeDateModel
import kotlinx.coroutines.flow.Flow

interface DateTimeRepository {
  val currentTimeDay: Int
  val currentTimeMonth: Int
  val currentTimeYear: Int
  suspend fun addTime()
  suspend fun calculateToday()
  fun getTimes(): Flow<List<TimeDateModel>>
  suspend fun getTimesMonth(yearNumber: Int, monthNumber: Int): List<TimeDateModel>
  fun getCurrentNameDay(date: String, format: String): String
  suspend fun updateDayToToday(day: Int, year: Int, month: Int)
}
