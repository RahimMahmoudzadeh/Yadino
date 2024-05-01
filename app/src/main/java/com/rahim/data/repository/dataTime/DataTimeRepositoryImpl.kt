package com.rahim.data.repository.dataTime

import com.rahim.data.db.dao.TimeDao
import com.rahim.data.di.DefaultDispatcher
import com.rahim.data.di.IODispatcher
import com.rahim.data.modle.data.TimeData
import com.rahim.utils.Constants.END_YEAR
import com.rahim.utils.Constants.FIRST_KABISE_DATA
import com.rahim.utils.Constants.FIRST_YEAR
import com.rahim.utils.enums.HalfWeekName
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
    private val timeDao: TimeDao
) :
    DataTimeRepository {
    private val persianData = PersianDate()
    private val currentTimeDay = persianData.shDay
    private val currentTimeMonth = persianData.shMonth
    private val currentTimeYer = persianData.shYear

    override suspend fun addTime() {
        if (!timeDao.getAllTime().isNullOrEmpty())
            return
        timeDao.insertAllTime(calculateDate())
    }

    override suspend fun calculateToday() {
        val today = timeDao.getToday()
        today?.let {
            if (checkDayIsToday(today.yerNumber, today.monthNumber, today.dayNumber))
                return
            timeDao.updateDayToNotToday(today.dayNumber, today.yerNumber, today.monthNumber)
            timeDao.updateDayToToday(currentTimeDay, currentTimeYer, currentTimeMonth)
        } ?: run {
            timeDao.updateDayToToday(currentTimeDay, currentTimeYer, currentTimeMonth)
        }
    }

    override fun getTimes(): Flow<List<TimeData>> = flow{
        timeDao.getAllTimeFlow().catch {}.collect{
            val spaceStart = calculateDaySpaceStartMonth(it[0].nameDay)
            val spaceEnd = calculateDaySpaceEndMonth(it[it.size - 1].nameDay)
            val list = ArrayList<TimeData>()
            list.addAll(spaceStart)
            list.addAll(it)
            list.addAll(spaceEnd)
            emit(list)
        }
    }.flowOn(ioDispatcher)

    private fun calculateDaySpaceStartMonth(nameDay: String): List<TimeData> {
        return when (nameDay) {
            HalfWeekName.FRIDAY.nameDay -> {
                listOf(
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null)
                )
            }

            HalfWeekName.THURSDAY.nameDay -> {
                listOf(
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null),
                )
            }

            HalfWeekName.WEDNESDAY.nameDay -> {
                listOf(
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null),
                )
            }

            HalfWeekName.TUESDAY.nameDay -> {
                listOf(
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null),
                )
            }

            HalfWeekName.MONDAY.nameDay -> {
                listOf(
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null),
                )
            }

            HalfWeekName.SUNDAY.nameDay -> {
                listOf(
                    TimeData(0, false, false, "", 0, 0, false, null),
                )
            }

            HalfWeekName.SATURDAY.nameDay -> {
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
            HalfWeekName.SATURDAY.nameDay -> {
                listOf(
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null)
                )
            }

            HalfWeekName.SUNDAY.nameDay -> {
                listOf(
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null),
                )
            }

            HalfWeekName.MONDAY.nameDay -> {
                listOf(
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null),
                )
            }

            HalfWeekName.TUESDAY.nameDay -> {
                listOf(
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null),
                )
            }

            HalfWeekName.WEDNESDAY.nameDay -> {
                listOf(
                    TimeData(0, false, false, "", 0, 0, false, null),
                    TimeData(0, false, false, "", 0, 0, false, null),
                )
            }

            HalfWeekName.THURSDAY.nameDay -> {
                listOf(
                    TimeData(0, false, false, "", 0, 0, false, null),
                )
            }

            HalfWeekName.FRIDAY.nameDay -> {
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
            var index = 4
            var differentBetweenYer = 0
            for (yer in FIRST_YEAR..END_YEAR) {
                if (yer >= FIRST_KABISE_DATA) {
                    differentBetweenYer += 1
                }
                for (month in 1..12) {
                    val dayNumber = if (month == 12) {
                        if ((((index % 4 == 0 && yer != 1308) || yer == FIRST_KABISE_DATA) && differentBetweenYer !in 29..32) || differentBetweenYer == 33) {
                            if (yer == FIRST_KABISE_DATA || differentBetweenYer == 33) {
                                differentBetweenYer = 0
                                index = 0
                            }
                            30
                        } else
                            29
                    } else if (month in 7..11) 30 else 31
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
                index += 1
            }
            timeDates
        }
    }

    private fun calculateDay(shDay: String): String {
        return when (shDay) {
            WeekName.SATURDAY.nameDay -> {
                HalfWeekName.SATURDAY.nameDay
            }

            WeekName.SUNDAY.nameDay -> {
                HalfWeekName.SUNDAY.nameDay
            }

            WeekName.MONDAY.nameDay -> {
                HalfWeekName.MONDAY.nameDay
            }

            WeekName.TUESDAY.nameDay -> {
                HalfWeekName.TUESDAY.nameDay
            }

            WeekName.WEDNESDAY.nameDay -> {
                HalfWeekName.WEDNESDAY.nameDay
            }

            WeekName.THURSDAY.nameDay -> {
                HalfWeekName.THURSDAY.nameDay
            }

            WeekName.FRIDAY.nameDay -> {
                HalfWeekName.FRIDAY.nameDay
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