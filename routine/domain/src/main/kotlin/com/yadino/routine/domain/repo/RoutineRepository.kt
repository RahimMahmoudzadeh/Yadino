package com.yadino.routine.domain.repo

import com.rahim.yadino.base.Resource
import com.rahim.yadino.enums.error.ErrorMessageCode
import com.yadino.routine.domain.model.RoutineDomainLayer
import kotlinx.coroutines.flow.Flow

interface RoutineRepository {
  suspend fun addSampleRoutine()
  suspend fun addRoutine(routineDomainLayer: RoutineDomainLayer)
  suspend fun removeRoutine(routineDomainLayer: RoutineDomainLayer): Int
  suspend fun removeAllRoutine(nameMonth: Int?, dayNumber: Int?, yearNumber: Int?)
  fun updateRoutine(routineDomainLayer: RoutineDomainLayer): Flow<Resource<RoutineDomainLayer, ErrorMessageCode>>
  suspend fun checkedRoutine(routineDomainLayer: RoutineDomainLayer)
  suspend fun getRoutine(id: Int): RoutineDomainLayer
  fun getRoutines(monthNumber: Int, numberDay: Int, yearNumber: Int): Flow<List<RoutineDomainLayer>>
  fun searchRoutine(name: String, yearNumber: Int?, monthNumber: Int?, dayNumber: Int?): Flow<List<RoutineDomainLayer>>
  suspend fun changeRoutineId()
  suspend fun checkedAllRoutinePastTime()
  suspend fun getAllRoutine(): List<RoutineDomainLayer>
  fun haveAlarm(): Flow<Boolean>
  suspend fun getRoutineAlarmId(): Long
  fun convertDateToMilSecond(yearNumber: Int?, monthNumber: Int?, dayNumber: Int?, timeHours: String?): Long
  suspend fun checkEqualRoutine(routineDomainLayer: RoutineDomainLayer): RoutineDomainLayer?
}
