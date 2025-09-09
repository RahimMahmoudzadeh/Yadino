package com.rahim.home.domain.dateTime

import com.rahim.home.domain.model.TimeDateDomainLayerHome
import kotlinx.coroutines.flow.Flow

interface DateTimeRepository {
  val currentTimeDay: Int
  val currentTimeMonth: Int
  val currentTimeYear: Int
  suspend fun addTime()
  suspend fun calculateToday()
  fun getTimes(): Flow<List<TimeDateDomainLayerHome>>
  suspend fun getTimesMonth(yearNumber: Int, monthNumber: Int): List<TimeDateDomainLayerHome>
  fun getCurrentNameDay(date: String, format: String): String
  suspend fun updateDayToToday(day: Int, year: Int, month: Int)
}
