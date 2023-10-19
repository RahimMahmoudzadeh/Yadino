package com.rahim.data.repository.routine

import com.rahim.data.modle.Rotin.Routine
import com.rahim.utils.resours.Resource
import kotlinx.coroutines.flow.Flow

interface RepositoryRoutine {
    suspend fun addSampleRoutine()
    suspend fun addRoutine(routine: Routine):Flow<Resource<Long>>

    suspend fun removeRoutine(routine: Routine):Int

    suspend fun removeAllRoutine(nameMonth: Int?, dayNumber: Int?, yerNumber: Int?)

    suspend fun updateRoutine(routine: Routine)

    suspend fun getRoutine(id: Int): Routine

    fun getRoutines(monthNumber: Int, numberDay: Int, yerNumber:Int): Flow<List<Routine>>

    fun searchRoutine(name: String,monthNumber: Int?, dayNumber: Int?): Flow<List<Routine>>

    suspend fun getCurrentRoutines(): Flow<List<Routine>>
}