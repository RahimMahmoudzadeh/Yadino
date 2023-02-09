package com.rahim.data.repository.dataTime

import com.rahim.data.db.database.AppDatabase
import com.rahim.data.modle.data.TimeData
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class DataTimeRepositoryImpl @Inject constructor(private val appDatabase: AppDatabase) :
    DataTimeRepository {

    private val dataTime = PersianDate()
    private var todayNumber: Int? = null
    private var dayName: String? = null
    private var yerNumber: Int? = null
    private var monthName: String? = null
    private val timeList = ArrayList<TimeData>()
    private val timesList = mutableListOf<TimeData>()
    private var firstTime = false
    private var previousName: String? = null

    init {
        CoroutineScope(Dispatchers.IO).launch {
            getTodayNumber()
            getDayName()
            getYerNumber()
            getMonthName()
            checkDayNumber()
            updateIsToday()
        }
    }

    override suspend fun getTodayNumber(): Int {
        todayNumber = dataTime.shDay
        return dataTime.shDay
    }

    override suspend fun getMonthLength(): String {
        return dataTime.monthDays.toString()
    }

    override suspend fun addAllTime(timesData: List<TimeData>) {
        appDatabase.timeDataDao().insertAllTime(timesData)
    }

    override suspend fun addTime(timeData: TimeData) {

    }

    override suspend fun getTime(): List<TimeData> {
        val times = appDatabase.timeDataDao().getAllMonthDay()
        if (timesList.isNotEmpty()) timesList.clear()

        timesList.addAll(times)
        checkFirstItemTimesList(times, 0)
        checkEndItemTimesList(times, times.size.minus(1))
        return timesList
    }

    private fun checkFirstItemTimesList(times: List<TimeData>, index: Int) {
        when (times[index].nameDay) {
            WeekName.SUNDAY.nameDay -> {
                setDayInFirstList(1)
            }
            WeekName.MONDAY.nameDay -> {
                setDayInFirstList(2)
            }
            WeekName.TUESDAY.nameDay -> {
                setDayInFirstList(3)
            }
            WeekName.WEDNESDAY.nameDay -> {
                setDayInFirstList(4)
            }
            WeekName.THURSDAY.nameDay -> {
                setDayInFirstList(5)
            }
            WeekName.FRIDAY.nameDay -> {
                setDayInFirstList(6)
            }
        }
    }

    private fun checkEndItemTimesList(times: List<TimeData>, index: Int) {
        when (times[index].nameDay) {
            WeekName.SATURDAY.nameDay -> {
                setDayInEndList(6)
            }
            WeekName.SUNDAY.nameDay -> {
                setDayInEndList(5)
            }
            WeekName.MONDAY.nameDay -> {
                setDayInEndList(4)
            }
            WeekName.TUESDAY.nameDay -> {
                setDayInEndList(3)
            }
            WeekName.WEDNESDAY.nameDay -> {
                setDayInEndList(2)
            }
            WeekName.THURSDAY.nameDay -> {
                setDayInEndList(1)
            }
        }
    }

    private fun setDayInFirstList(cuntFor: Int) {
        val timeData = TimeData(
            0, false, false, null, null, null, false
        )
        for (i in 0..cuntFor.minus(1)) {
            timesList.add(i, timeData)
        }
    }

    private fun setDayInEndList(cuntFor: Int) {
        val timeData = TimeData(
            0, false, false, null, null, null, false
        )
        for (i in 0..cuntFor.minus(1)) {
            timesList.add(timeData)
        }
    }

    override suspend fun getDayName(): String {
        dayName = dataTime.dayName()
        return dataTime.dayName()
    }

    override suspend fun getYerNumber(): Int {
        yerNumber = dataTime.shYear
        return dataTime.shYear
    }

    override suspend fun getMonthName(): String {
        monthName = dataTime.monthName()
        return dataTime.monthName()
    }

    override suspend fun calculationDayNameBeforeCurrentDay() {
        if (timeList.isNotEmpty()) timeList.clear()

        todayNumber?.let { currentDay ->
            for (i in currentDay.minus(1).downTo(1)) {
                val timeData = TimeData(
                    i,
                    false,
                    isToday = false,
                    nameDay = calculatePreviousName(dayName.toString()),
                    yerNumber = yerNumber,
                    month = monthName.toString(),
                    isChecked = false
                )
                timeList.add(timeData)
            }
            addAllTime(timeList.reversed())
            firstTime = false

        }
        calculationDayNameAfterCurrentDay()
    }

    override suspend fun calculationDayNameAfterCurrentDay() {
        if (timeList.isNotEmpty()) timeList.clear()

        todayNumber?.let { currentName ->
            for (i in currentName..dataTime.monthLength) {
                val timeData = TimeData(
                    i,
                    false,
                    isToday = false,
                    nameDay = calculateNextName(dayName.toString()),
                    yerNumber = yerNumber,
                    month = monthName.toString(),
                    isChecked = false
                )
                timeList.add(timeData)
            }
            addAllTime(timeList)
        }
    }

    override suspend fun updateTime(timeData: TimeData) {
        appDatabase.timeDataDao().updateTimeData(timeData)
    }


    private suspend fun checkDayNumber() {
        val isTime =
            appDatabase.timeDataDao().getIsSpecificMonthFromYer(monthName.toString(), yerNumber)
        if (isTime) return

        if (getTodayNumber() > 1) {
            calculationDayNameBeforeCurrentDay()
        } else {
            calculationDayNameAfterCurrentDay()
        }
    }

    private fun calculateNextName(currentName: String): String {
        return if (!firstTime) {
            firstTime = true
            previousName = currentName
            currentName
        } else {
            when (previousName) {
                WeekName.SATURDAY.nameDay -> {
                    previousName = WeekName.SUNDAY.nameDay
                    WeekName.SUNDAY.nameDay
                }
                WeekName.SUNDAY.nameDay -> {
                    previousName = WeekName.MONDAY.nameDay
                    WeekName.MONDAY.nameDay
                }
                WeekName.MONDAY.nameDay -> {
                    previousName = WeekName.TUESDAY.nameDay
                    WeekName.TUESDAY.nameDay
                }
                WeekName.TUESDAY.nameDay -> {
                    previousName = WeekName.WEDNESDAY.nameDay
                    WeekName.WEDNESDAY.nameDay
                }
                WeekName.WEDNESDAY.nameDay -> {
                    previousName = WeekName.THURSDAY.nameDay
                    WeekName.THURSDAY.nameDay
                }
                WeekName.THURSDAY.nameDay -> {
                    previousName = WeekName.FRIDAY.nameDay
                    WeekName.FRIDAY.nameDay
                }
                else -> {
                    previousName = WeekName.SATURDAY.nameDay
                    WeekName.SATURDAY.nameDay
                }
            }
        }
    }

    private fun calculatePreviousName(currentName: String): String {
        if (!firstTime) {
            firstTime = true
            previousName = currentName
        }
        return when (previousName) {
            WeekName.SATURDAY.nameDay -> {
                previousName = WeekName.FRIDAY.nameDay
                WeekName.FRIDAY.nameDay
            }
            WeekName.SUNDAY.nameDay -> {
                previousName = WeekName.SATURDAY.nameDay
                WeekName.SATURDAY.nameDay
            }
            WeekName.MONDAY.nameDay -> {
                previousName = WeekName.SUNDAY.nameDay
                WeekName.SUNDAY.nameDay
            }
            WeekName.TUESDAY.nameDay -> {
                previousName = WeekName.MONDAY.nameDay
                WeekName.MONDAY.nameDay
            }
            WeekName.WEDNESDAY.nameDay -> {
                previousName = WeekName.TUESDAY.nameDay
                WeekName.TUESDAY.nameDay
            }
            WeekName.THURSDAY.nameDay -> {
                previousName = WeekName.WEDNESDAY.nameDay
                WeekName.WEDNESDAY.nameDay
            }
            else -> {
                previousName = WeekName.THURSDAY.nameDay
                WeekName.THURSDAY.nameDay
            }
        }
    }

    private suspend fun updateIsToday() {
        val times = appDatabase.timeDataDao().getAllMonthDayNotFlow()
        times.let { time ->
            for (i in 1 until time.size) {
                time[i].let { timeI ->
                    if (timeI.isToday) {
                        val oldTime = TimeData(
                            timeI.dayNumber,
                            timeI.haveTask,
                            false,
                            timeI.nameDay,
                            timeI.yerNumber,
                            timeI.month,
                            false,
                            timeI.id
                        )
                        appDatabase.timeDataDao().updateTimeData(oldTime)
                    }
                    if (timeI.dayNumber == todayNumber && timeI.nameDay == dayName && timeI.yerNumber == yerNumber && timeI.month == monthName) {
                        val newTime = TimeData(
                            timeI.dayNumber,
                            timeI.haveTask,
                            true,
                            timeI.nameDay,
                            timeI.yerNumber,
                            timeI.month,
                            true,
                            timeI.id
                        )
                        appDatabase.timeDataDao().updateTimeData(newTime)
                    }
                }
            }
        }
    }
}