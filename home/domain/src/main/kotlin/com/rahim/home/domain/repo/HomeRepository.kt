package com.rahim.home.domain.repo

import com.rahim.home.domain.model.CurrentDate
import com.rahim.home.domain.model.Routine
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
  suspend fun addRoutine(routineModel: Routine)
  suspend fun removeRoutine(routineModel: Routine)
  suspend fun updateRoutine(routineModel: Routine)
  suspend fun checkedRoutine(routineModel: Routine)
  suspend fun getRoutine(id: Int): Routine
  suspend fun checkEqualRoutine(routineModel: Routine): Routine?
  suspend fun getRoutineAlarmId(): Long
  fun getCurrentDate(): CurrentDate
  fun getTodayRoutines(): Flow<List<Routine>>
  fun searchTodayRoutine(name: String): Flow<List<Routine>>
  fun convertDateToMilSecond(year: Int?, month: Int?, day: Int?, hours: String?): Long
}
