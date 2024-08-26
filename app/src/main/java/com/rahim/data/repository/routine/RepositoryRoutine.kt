package com.rahim.data.repository.routine

import com.rahim.data.model.routine.RoutineModel
import com.rahim.utils.resours.Resource
import kotlinx.coroutines.flow.Flow

interface RepositoryRoutine {
  suspend fun addSampleRoutine()
  fun addRoutine(routineModel: RoutineModel): Flow<Resource<RoutineModel?>>

  suspend fun removeRoutine(routineModel: RoutineModel): Int

  suspend fun removeAllRoutine(nameMonth: Int?, dayNumber: Int?, yerNumber: Int?)

  suspend fun updateRoutine(routineModel: RoutineModel): Flow<Resource<RoutineModel?>>
  suspend fun checkedRoutine(routineModel: RoutineModel)

  suspend fun getRoutine(id: Int): RoutineModel

  fun getRoutines(monthNumber: Int, numberDay: Int, yerNumber: Int): Flow<List<RoutineModel>>

  fun searchRoutine(name: String, monthNumber: Int?, dayNumber: Int?): Flow<List<RoutineModel>>

  suspend fun changeRoutineId()
  suspend fun checkEdAllRoutinePastTime()

  suspend fun getAllRoutine(): List<RoutineModel>
}
