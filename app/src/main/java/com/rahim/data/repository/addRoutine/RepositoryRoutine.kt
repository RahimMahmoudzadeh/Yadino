package com.rahim.data.repository.addRoutine

import com.rahim.data.modle.Rotin.Routine
import kotlinx.coroutines.flow.Flow

interface RepositoryRoutine {

    suspend fun addRoutine(routine: Routine)

    suspend fun removeRoutine(routine: Routine)

    suspend fun removeAllRoutine(nameMonth: String?, dayNumber: String?, yerNumber: String?)

    suspend fun updateRoutine(routine: Routine)

    suspend fun getRoutine(id: Int): Routine

    fun getRoutine(monthName: String, numberDay: Int): Flow<List<Routine>>

    fun searchRoutine(name: String, nameMonth: String?, dayNumber: String?): Flow<List<Routine>>
}