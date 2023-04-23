package com.rahim.data.repository.home

import com.rahim.data.modle.Rotin.Routine
import com.rahim.utils.resours.Resource
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getCurrentTime(): List<Int>

    suspend fun getCurrentRoutines(): Flow<List<Routine>>

    suspend fun updateRoutine(routine: Routine)

    suspend fun deleteRoutine(routine: Routine)

    suspend fun addRoutine(routine: Routine)
}