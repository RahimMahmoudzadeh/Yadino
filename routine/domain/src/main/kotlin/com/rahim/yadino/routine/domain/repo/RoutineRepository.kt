package com.rahim.yadino.routine.domain.repo

import com.rahim.yadino.base.Resource
import com.rahim.yadino.enums.error.ErrorMessageCode
import com.rahim.yadino.routine.domain.model.Routine
import kotlinx.coroutines.flow.Flow

interface RoutineRepository {
  suspend fun addSampleRoutine()
  suspend fun addRoutine(routine: Routine)
  suspend fun removeRoutine(routine: Routine): Int
  suspend fun removeAllRoutine(nameMonth: Int?, dayNumber: Int?, yearNumber: Int?)
  fun updateRoutine(routine: Routine): Flow<Resource<Routine, ErrorMessageCode>>
  suspend fun checkedRoutine(routine: Routine)
  suspend fun getRoutine(id: Int): Routine
  fun getRoutines(monthNumber: Int, numberDay: Int, yearNumber: Int): Flow<List<Routine>>
  fun searchRoutine(name: String, yearNumber: Int?, monthNumber: Int?, dayNumber: Int?): Flow<List<Routine>>
  suspend fun changeRoutineId()
  suspend fun checkedAllRoutinePastTime()
  suspend fun getAllRoutine(): List<Routine>
  fun haveAlarm(): Flow<Boolean>
  suspend fun getRoutineAlarmId(): Int
  fun convertDateToMilSecond(yearNumber: Int?, monthNumber: Int?, dayNumber: Int?, timeHours: String?): Long
  suspend fun checkEqualRoutine(routine: Routine): Routine?
}
