package com.yadino.routine.domain

import com.rahim.home.domain.model.RoutineModel
import com.rahim.yadino.Resource
import kotlinx.coroutines.flow.Flow

interface RoutineRepository {
  suspend fun addSampleRoutine()
  suspend fun addRoutine(routineModel: RoutineModel)
  suspend fun removeRoutine(routineModel: RoutineModel): Int
  suspend fun removeAllRoutine(nameMonth: Int?, dayNumber: Int?, yearNumber: Int?)
  fun updateRoutine(routineModel: RoutineModel): Flow<Resource<RoutineModel?>>
  suspend fun checkedRoutine(routineModel: RoutineModel)
  suspend fun getRoutine(id: Int): RoutineModel
  fun getRoutines(monthNumber: Int, numberDay: Int, yearNumber: Int): Flow<List<RoutineModel>>
  fun searchRoutine(name: String, yearNumber: Int?, monthNumber: Int?, dayNumber: Int?): Flow<List<RoutineModel>>
  suspend fun changeRoutineId()
  suspend fun checkedAllRoutinePastTime()
  suspend fun getAllRoutine(): List<RoutineModel>
  fun haveAlarm(): Flow<Boolean>
  suspend fun getRoutineAlarmId(): Long
  fun convertDateToMilSecond(yearNumber: Int?, monthNumber: Int?, dayNumber: Int?, timeHours: String?): Long
  suspend fun checkEqualRoutine(routineModel: RoutineModel): RoutineModel?
}
