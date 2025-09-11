package com.rahim.home.domain.repo

import com.rahim.home.domain.model.CurrentDateDomainLayer
import com.rahim.home.domain.model.RoutineHomeDomainLayer
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
  suspend fun addRoutine(routineModel: RoutineHomeDomainLayer)
  suspend fun removeRoutine(routineModel: RoutineHomeDomainLayer)
  suspend fun updateRoutine(routineModel: RoutineHomeDomainLayer)
  suspend fun checkedRoutine(routineModel: RoutineHomeDomainLayer)
  suspend fun getRoutine(id: Int): RoutineHomeDomainLayer
  suspend fun checkEqualRoutine(routineModel: RoutineHomeDomainLayer): RoutineHomeDomainLayer?
  suspend fun getRoutineAlarmId(): Long
  fun getCurrentDate(): CurrentDateDomainLayer
  fun getTodayRoutines(): Flow<List<RoutineHomeDomainLayer>>
  fun searchTodayRoutine(name: String): Flow<List<RoutineHomeDomainLayer>>
  fun convertDateToMilSecond(year: Int?, month: Int?, day: Int?, hours: String?): Long
}
