package com.rahim.home.domain.repo

import com.rahim.home.domain.model.CurrentDateModel
import com.rahim.home.domain.model.RoutineHomeModel
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
  suspend fun addRoutine(routineModel: RoutineHomeModel)
  suspend fun removeRoutine(routineModel: RoutineHomeModel)
  suspend fun updateRoutine(routineModel: RoutineHomeModel)
  suspend fun checkedRoutine(routineModel: RoutineHomeModel)
  suspend fun getRoutine(id: Int): RoutineHomeModel
  suspend fun checkEqualRoutine(routineModel: RoutineHomeModel): RoutineHomeModel?
  suspend fun getRoutineAlarmId(): Long
  fun getCurrentDate(): CurrentDateModel
  fun getTodayRoutines(): Flow<List<RoutineHomeModel>>
  fun searchTodayRoutine(name: String): Flow<List<RoutineHomeModel>>
  fun convertDateToMilSecond(year: Int?, month: Int?, day: Int?, hours: String?): Long
}
