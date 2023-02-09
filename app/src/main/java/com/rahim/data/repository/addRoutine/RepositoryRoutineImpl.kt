package com.rahim.data.repository.addRoutine

import com.rahim.data.db.database.AppDatabase
import com.rahim.data.modle.Rotin.Routine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RepositoryRoutineImpl @Inject constructor(val appDatabase: AppDatabase) : RepositoryRoutine {
    override suspend fun addRoutine(routine: Routine) {
        appDatabase.taskDao().addRoutine(routine)
    }

    override suspend fun removeRoutine(routine: Routine) {
        appDatabase.taskDao().removeRoutine(routine)
    }

    override suspend fun removeAllRoutine(nameMonth: String?, dayNumber: String?, yerNumber: String?) {
        appDatabase.taskDao().removeAllRoutine(nameMonth, dayNumber, yerNumber)
    }

    override suspend fun updateRoutine(routine: Routine) {
        appDatabase.taskDao().updateRoutine(routine)
    }

    override suspend fun getRoutine(id: Int): Routine = appDatabase.taskDao().getRoutine(id)


    override fun getRoutine(monthName: String, numberDay: Int): Flow<List<Routine>> = appDatabase.taskDao().getRoutines(monthName, numberDay).distinctUntilChanged()

    override fun searchRoutine(name: String,nameMonth: String?, dayNumber: String?): Flow<List<Routine>> = appDatabase.taskDao().searchRoutine(name,nameMonth, dayNumber).distinctUntilChanged()

}