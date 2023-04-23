package com.rahim.data.repository.home

import com.rahim.data.db.database.AppDatabase
import com.rahim.data.modle.Rotin.Routine
import kotlinx.coroutines.flow.Flow
import saman.zamani.persiandate.PersianDate
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(val appDatabase: AppDatabase) : HomeRepository {
    private val persianData = PersianDate()
    private val currentTimeDay = persianData.shDay
    private val currentTimeMonth = persianData.shMonth
    private val currentTimeYer = persianData.shYear

    override fun getCurrentTime(): List<Int> = listOf(currentTimeYer,currentTimeMonth,currentTimeDay)

    override suspend fun getCurrentRoutines(): Flow<List<Routine>> {
        return appDatabase.routineDao().getRoutines(currentTimeMonth,currentTimeDay,currentTimeYer)
    }

    override suspend fun updateRoutine(routine: Routine) {
        appDatabase.routineDao().updateRoutine(routine)
    }

    override suspend fun deleteRoutine(routine: Routine) {
        appDatabase.routineDao().removeRoutine(routine)
    }

    override suspend fun addRoutine(routine: Routine) {
        appDatabase.routineDao().addRoutine(routine)
    }
}