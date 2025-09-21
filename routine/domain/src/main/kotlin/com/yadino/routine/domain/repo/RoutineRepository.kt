package com.yadino.routine.domain.repo

import com.rahim.yadino.Resource
import com.yadino.routine.domain.model.RoutineModelDomainLayer
import kotlinx.coroutines.flow.Flow

interface RoutineRepository {
  suspend fun addSampleRoutine()
  suspend fun addRoutine(routineModelDomainLayer: RoutineModelDomainLayer)
  suspend fun removeRoutine(routineModelDomainLayer: RoutineModelDomainLayer): Int
  suspend fun removeAllRoutine(nameMonth: Int?, dayNumber: Int?, yearNumber: Int?)
  fun updateRoutine(routineModelDomainLayer: RoutineModelDomainLayer): Flow<Resource<RoutineModelDomainLayer?>>
  suspend fun checkedRoutine(routineModelDomainLayer: RoutineModelDomainLayer)
  suspend fun getRoutine(id: Int): RoutineModelDomainLayer
  fun getRoutines(monthNumber: Int, numberDay: Int, yearNumber: Int): Flow<List<RoutineModelDomainLayer>>
  fun searchRoutine(name: String, yearNumber: Int?, monthNumber: Int?, dayNumber: Int?): Flow<List<RoutineModelDomainLayer>>
  suspend fun changeRoutineId()
  suspend fun checkedAllRoutinePastTime()
  suspend fun getAllRoutine(): List<RoutineModelDomainLayer>
  fun haveAlarm(): Flow<Boolean>
  suspend fun getRoutineAlarmId(): Long
  fun convertDateToMilSecond(yearNumber: Int?, monthNumber: Int?, dayNumber: Int?, timeHours: String?): Long
  suspend fun checkEqualRoutine(routineModelDomainLayer: RoutineModelDomainLayer): RoutineModelDomainLayer?
}
