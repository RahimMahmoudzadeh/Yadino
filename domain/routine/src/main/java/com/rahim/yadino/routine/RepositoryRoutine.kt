package com.rahim.yadino.routine

import com.rahim.yadino.Resource
import com.rahim.yadino.model.RoutineModel
import kotlinx.coroutines.flow.Flow

interface RepositoryRoutine {
    suspend fun addSampleRoutine()
    suspend fun addRoutine(routineModel: RoutineModel)
    suspend fun removeRoutine(routineModel: RoutineModel): Int
    suspend fun removeAllRoutine(nameMonth: Int?, dayNumber: Int?, yearNumber: Int?)
    fun updateRoutine(routineModel: RoutineModel): Flow<Resource<RoutineModel?>>
    suspend fun checkedRoutine(routineModel: RoutineModel)
    suspend fun getRoutine(id: Int): RoutineModel
    fun getRoutines(monthNumber: Int, numberDay: Int, yearNumber: Int): Flow<List<RoutineModel>>
    suspend fun searchRoutine(name: String, yearNumber: Int?,monthNumber: Int?, dayNumber: Int?): List<RoutineModel>
    suspend fun changeRoutineId()
    suspend fun checkedAllRoutinePastTime()
    suspend fun getAllRoutine(): List<RoutineModel>
    fun haveAlarm(): Flow<Boolean>
    suspend fun getRoutineAlarmId(): Long
    fun convertDateToMilSecond(
        yearNumber: Int?,
        monthNumber: Int?,
        dayNumber: Int?,
        timeHours: String?
    ): Long
    suspend fun checkEqualRoutine(routineModel: RoutineModel): RoutineModel?
}
