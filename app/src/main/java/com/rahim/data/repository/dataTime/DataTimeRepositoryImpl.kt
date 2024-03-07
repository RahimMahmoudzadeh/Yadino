package com.rahim.data.repository.dataTime

import com.rahim.data.db.database.AppDatabase
import com.rahim.data.di.DefaultDispatcher
import com.rahim.data.di.IODispatcher
import com.rahim.data.modle.data.TimeData
import com.rahim.utils.Constants.END_YEAR
import com.rahim.utils.Constants.FIRST_YEAR
import com.rahim.utils.enums.WeekName
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import saman.zamani.persiandate.PersianDate
import javax.inject.Inject
import kotlin.collections.ArrayList


class DataTimeRepositoryImpl @Inject constructor(
    @DefaultDispatcher val defaultDispatcher: CoroutineDispatcher,
    @IODispatcher val ioDispatcher: CoroutineDispatcher,
    private val appDatabase: AppDatabase
) :
    DataTimeRepository {
    private val timeDao = appDatabase.timeDataDao()
    private val persianData = PersianDate()
    private val currentTimeDay = persianData.shDay
    private val currentTimeMonth = persianData.shMonth
    private val currentTimeYer = persianData.shYear

    override suspend fun addTime() {
        if (!timeDao.getAllTime().isNullOrEmpty())
            return
        appDatabase.timeDataDao().insertAllTime(calculateDate())
    }

    override suspend fun calculateToday() {
        val times = timeDao.getAllTime()
        val today = timeDao.getToday()

        if (times.isEmpty())
            return

        if (today == null) {
            val currentDay =
                times.find { it.dayNumber == currentTimeDay && it.monthNumber == currentTimeMonth && it.yerNumber == currentTimeYer }
            currentDay?.let {
                it.apply {
                    isToday = true
                }
                timeDao.updateTimeData(currentDay)
            }
            return
        }

        if (checkDayIsToday(today.yerNumber, today.monthNumber, today.dayNumber))
            return

        times.forEach {
            if (it.isToday && !checkDayIsToday(
                    today.yerNumber,
                    today.monthNumber,
                    today.dayNumber
                )
            ) {
                it.apply {
                    isToday = false
                }
                timeDao.updateTimeData(it)
            }
            if (checkDayIsToday(today.yerNumber, today.monthNumber, today.dayNumber)) {
                it.apply {
                    isToday = true
                }
                timeDao.updateTimeData(it)
            }
        }
    }

    override suspend fun getCurrentMonthDay(
        monthNumber: Int,
        yerNumber: Int?
    ): Flow<List<TimeData>> = flow {
        appDatabase.timeDataDao().getSpecificMonthFromYer(monthNumber, yerNumber).catch {}.collect {
            if (it.isNotEmpty()){
                val spaceStart = calculateDaySpaceStartMonth(it[0].nameDay)
                val spaceEnd = calculateDaySpaceEndMonth(it[it.size-1].nameDay)
                val list = ArrayList<TimeData>()
                list.addAll(spaceStart)
                list.addAll(it)
                list.addAll(spaceEnd)
                emit(list)
            }
        }
    }.flowOn(ioDispatcher)

    private fun calculateDaySpaceStartMonth(nameDay: String): List<TimeData> {
        return when (nameDay) {
            "ج" -> {
                listOf(
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null)
                )
            }

            "پ" -> {
                listOf(
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null),
                )
            }

            "چ" -> {
                listOf(
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null),
                )
            }

            "س" -> {
                listOf(
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null),
                )
            }

            "د" -> {
                listOf(
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null),
                )
            }

            "ی" -> {
                listOf(
                    TimeData(0, false, false, "", 0, 0, false, null),
                )
            }

            "ش" -> {
                listOf()
            }

            else -> {
                listOf(
                    TimeData(0, false, false, "", 0, 0, false, null),
                )
            }
        }
    }
    private fun calculateDaySpaceEndMonth(nameDay: String): List<TimeData> {
        return when (nameDay) {
            "ش" -> {
                listOf(
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null)
                )
            }

            "ی" -> {
                listOf(
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null),
                )
            }

            "د" -> {
                listOf(
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null),
                )
            }

            "س" -> {
                listOf(
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null),
                )
            }

            "چ" -> {
                listOf(
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null),
                )
            }

            "پ" -> {
                listOf(
                    TimeData(0, false, false, "", 0, 0, false, null),
                )
            }

            "ج" -> {
                listOf()
            }

            else -> {
                listOf(
                    TimeData(0, false, false, "", 0, 0, false, null),
                )
            }
        }
    }


    private suspend fun calculateDate(): List<TimeData> {
        return withContext(defaultDispatcher) {
            val timeDates = ArrayList<TimeData>()
            for (yer in FIRST_YEAR..END_YEAR) {
                for (month in 1..12) {
                    val dayNumber = if (month == 12) 29 else if (month in 7..11) 30 else 31
                    for (day in 1..dayNumber) {
                        persianData.initJalaliDate(yer, month, day)
                        val today = checkDayIsToday(yer, month, day)
                        val data = TimeData(
                            persianData.shDay,
                            false,
                            today,
                            calculateDay(persianData.dayName()),
                            persianData.shYear,
                            persianData.shMonth,
                            today
                        )
                        timeDates.add(data)
                    }
                }
            }
            timeDates
        }
    }

    private fun calculateDay(shDay: String): String {
        return when (shDay) {
            WeekName.SATURDAY.nameDay -> {
                "ش"
            }

            WeekName.SUNDAY.nameDay -> {
                "ی"
            }

            WeekName.MONDAY.nameDay -> {
                "د"
            }

            WeekName.TUESDAY.nameDay -> {
                "س"
            }

            WeekName.WEDNESDAY.nameDay -> {
                "چ"
            }

            WeekName.THURSDAY.nameDay -> {
                "پ"
            }

            WeekName.FRIDAY.nameDay -> {
                "ج"
            }

            else -> {
                ""
            }
        }
    }

    private fun checkDayIsToday(yer: Int, month: Int, day: Int): Boolean {
        if (yer != currentTimeYer)
            return false
        if (month != currentTimeMonth)
            return false
        if (day != currentTimeDay)
            return false

        return true
    }


}