package com.rahim.data.repository.home

import com.rahim.data.modle.Rotin.Routine
import com.rahim.utils.resours.Resource
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    suspend fun getCurrentRoutines(): Flow<List<Routine>>
}