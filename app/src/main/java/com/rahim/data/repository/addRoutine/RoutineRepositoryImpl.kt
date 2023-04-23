package com.rahim.data.repository.addRoutine

import com.rahim.data.db.database.AppDatabase
import com.rahim.data.modle.Rotin.Routine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class RoutineRepositoryImpl @Inject constructor(val appDatabase: AppDatabase) : RepositoryRoutine {
    override suspend fun addRoutine(routine: Routine) {
        appDatabase.routineDao().addRoutine(routine)
    }

    override suspend fun removeRoutine(routine: Routine) {
        appDatabase.routineDao().removeRoutine(routine)
    }

    override suspend fun removeAllRoutine(nameMonth: Int?, dayNumber: Int?, yerNumber: Int?) {
        appDatabase.routineDao().removeAllRoutine(nameMonth, dayNumber, yerNumber)
    }

    override suspend fun updateRoutine(routine: Routine) {
        appDatabase.routineDao().updateRoutine(routine)
    }

    override suspend fun getRoutine(id: Int): Routine = appDatabase.routineDao().getRoutine(id)


    override fun getRoutine(monthNumber: Int, numberDay: Int,yerNumber:Int): Flow<List<Routine>> = appDatabase.routineDao().getRoutines(monthNumber, numberDay,yerNumber).distinctUntilChanged()

    override fun searchRoutine(name: String,monthNumber: Int?, dayNumber: Int?): Flow<List<Routine>> = appDatabase.routineDao().searchRoutine(name,monthNumber, dayNumber).distinctUntilChanged()

}