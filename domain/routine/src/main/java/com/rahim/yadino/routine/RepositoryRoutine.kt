package com.rahim.yadino.routine

import com.rahim.yadino.base.Resource
import com.rahim.yadino.routine.modle.Routine.Routine
import kotlinx.coroutines.flow.Flow

interface RepositoryRoutine {
    suspend fun addSampleRoutine()
    fun addRoutine(routine: Routine): Flow<Resource<Routine?>>

    suspend fun removeRoutine(routine: Routine): Int

    suspend fun removeAllRoutine(nameMonth: Int?, dayNumber: Int?, yerNumber: Int?)

    suspend fun updateRoutine(routine: Routine): Flow<Resource<Routine?>>
    suspend fun checkedRoutine(routine: Routine)

    suspend fun getRoutine(id: Int): Routine

    fun getRoutines(monthNumber: Int, numberDay: Int, yerNumber: Int): Flow<List<Routine>>

    fun searchRoutine(name: String, monthNumber: Int?, dayNumber: Int?): Flow<List<Routine>>

    suspend fun changeRoutineId()
    suspend fun checkEdAllRoutinePastTime()

    suspend fun getAllRoutine(): List<Routine>
    fun haveAlarm(): Flow<Boolean>
}