package com.rahim.data.repository.routine

import com.rahim.data.db.database.AppDatabase
import com.rahim.data.modle.Rotin.Routine
import com.rahim.data.sharedPreferences.SharedPreferencesCustom
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import saman.zamani.persiandate.PersianDate
import javax.inject.Inject

class RoutineRepositoryImpl @Inject constructor(
    private val appDatabase: AppDatabase,
    private val sharedPreferencesCustom: SharedPreferencesCustom
) : RepositoryRoutine {
    private val persianData = PersianDate()
    private val currentTimeDay = persianData.shDay
    private val currentTimeMonth = persianData.shMonth
    private val currentTimeYer = persianData.shYear


    override suspend fun addSampleRoutine() {
        val sampleRoutines = appDatabase.routineDao()
            .getSampleRoutine(currentTimeMonth, currentTimeDay, currentTimeYer)
        if (sharedPreferencesCustom.isShowSampleRoutine()) {
            appDatabase.routineDao().removeSampleRoutine()
            return
        }

        if (sampleRoutines.isNotEmpty())
            return
        appDatabase.routineDao().removeSampleRoutine()
        (0..1).forEach {
            val routine = if (it == 0) {
                Routine(
                    "تست1",
                    0,
                    currentTimeDay.toString(),
                    currentTimeDay,
                    currentTimeMonth,
                    currentTimeYer,
                    "12:00",
                    false,
                    explanation = "من یک روتین تستی هستم لطفا من را به چپ بکشید",
                    isSample = true
                )
            } else {
                Routine(
                    "تست2",
                    0,
                    currentTimeDay.toString(),
                    currentTimeDay,
                    currentTimeMonth,
                    currentTimeYer,
                    "12:00",
                    false,
                    explanation = "من یک روتین تستی هستم لطفا من را به راست بکشید",
                    isSample = true
                )
            }
            addRoutine(routine)
        }
    }

    override suspend fun addRoutine(routine: Routine): Long {
        return appDatabase.routineDao().addRoutine(routine)
    }

    override suspend fun removeRoutine(routine: Routine): Int {
        return appDatabase.routineDao().removeRoutine(routine)
    }

    override suspend fun removeAllRoutine(nameMonth: Int?, dayNumber: Int?, yerNumber: Int?) {
        appDatabase.routineDao().removeAllRoutine(nameMonth, dayNumber, yerNumber)
    }

    override suspend fun updateRoutine(routine: Routine) {
        appDatabase.routineDao().updateRoutine(routine)
    }

    override suspend fun getRoutine(id: Int): Routine = appDatabase.routineDao().getRoutine(id)


    override fun getRoutine(monthNumber: Int, numberDay: Int, yerNumber: Int): Flow<List<Routine>> =
        appDatabase.routineDao().getRoutines(monthNumber, numberDay, yerNumber)
            .distinctUntilChanged()

    override fun searchRoutine(
        name: String,
        monthNumber: Int?,
        dayNumber: Int?
    ): Flow<List<Routine>> =
        appDatabase.routineDao().searchRoutine(name, monthNumber, dayNumber).distinctUntilChanged()

    override suspend fun getCurrentRoutines(): Flow<List<Routine>> {
        return appDatabase.routineDao()
            .getRoutines(currentTimeMonth, currentTimeDay, currentTimeYer)
    }
}