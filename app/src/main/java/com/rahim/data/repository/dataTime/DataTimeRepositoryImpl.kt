package com.rahim.data.repository.dataTime

import com.rahim.data.db.database.AppDatabase
import com.rahim.data.di.DefaultDispatcher
import com.rahim.data.modle.data.TimeData
import com.rahim.utils.enums.WeekName
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import saman.zamani.persiandate.PersianDate
import java.util.*
import javax.inject.Inject


class DataTimeRepositoryImpl @Inject constructor(
    @DefaultDispatcher val defaultDispatcher: CoroutineDispatcher,
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

    override suspend fun getCurrentMonthDay(
        monthNumber: Int,
        yerNumber: Int?
    ): Flow<List<TimeData>> =
        appDatabase.timeDataDao().getSpecificMonthFromYer(monthNumber, yerNumber)

    private suspend fun calculateDate(): List<TimeData> {
        return withContext(defaultDispatcher) {
            val timeDates = ArrayList<TimeData>()
            for (yer in 1300..1430) {
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

            else -> {""}
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