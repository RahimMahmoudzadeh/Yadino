package com.rahim.yadino.datetime_repository

import com.rahim.yadino.Constants.END_YEAR
import com.rahim.yadino.Constants.FIRST_YEAR
import com.rahim.yadino.Constants.VERSION_TIME_DB
import com.rahim.yadino.enums.HalfWeekName
import com.rahim.yadino.enums.WeekName
import com.rahim.yadino.dateTime.DateTimeRepository
import com.rahim.yadino.dateTime.dao.TimeDao
import com.rahim.yadino.dateTime.model.TimeDate
import com.rahim.yadino.di.DefaultDispatcher
import com.rahim.yadino.di.IODispatcher
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import saman.zamani.persiandate.PersianDate
import saman.zamani.persiandate.PersianDateFormat
import javax.inject.Inject


class DateTimeRepositoryImpl @Inject constructor(
  @DefaultDispatcher val defaultDispatcher: CoroutineDispatcher,
  @IODispatcher val ioDispatcher: CoroutineDispatcher,
  private val timeDao: TimeDao,
) :
  DateTimeRepository {
  private val persianData = PersianDate()
  override val currentTimeDay = persianData.shDay
  override val currentTimeYear = persianData.shYear
  override val currentTimeMonth = persianData.shMonth
  override suspend fun addTime() {
    val times = timeDao.getAllTime()
    val firstTime =
      times.find { it.yearNumber == FIRST_YEAR && it.versionNumber == VERSION_TIME_DB }
    if (firstTime != null) {
      return
    }
    if (times.isNotEmpty())
      timeDao.deleteAllTimes()

    calculateDate()
  }

  override suspend fun calculateToday() {
    val today = timeDao.getToday()
    today?.let {
      timeDao.updateDayToNotToday()
      timeDao.updateDayToToday(currentTimeDay, currentTimeYear, currentTimeMonth)
    } ?: run {
      timeDao.updateDayToToday(currentTimeDay, currentTimeYear, currentTimeMonth)
    }
  }

  override suspend fun updateDayToToday(day: Int, year: Int, month: Int) {
    timeDao.updateNotIsChecked()
    timeDao.updateIsChecked(day, year, month)
  }

  override fun getTimes(): Flow<List<TimeDate>> = timeDao.getAllTimeFlow()

  override suspend fun getTimesMonth(yearNumber: Int, monthNumber: Int): List<TimeDate> {
    val timesDb = timeDao.getSpecificMonthFromYear(monthNumber, yearNumber)
    return if (yearNumber != FIRST_YEAR || yearNumber != END_YEAR) {
      if (timesDb.isNotEmpty()) {
        val times = ArrayList<TimeDate>()
        val spaceStart = calculateDaySpaceStartMonth(timesDb.first())
        val spaceEnd = calculateDaySpaceEndMonth(timesDb.last())
        times.addAll(spaceStart)
        times.addAll(timesDb)
        times.addAll(spaceEnd)
        times
      } else {
        emptyList()
      }
    } else {
      timesDb
    }
  }


  private fun calculateDaySpaceStartMonth(timeDate: TimeDate): List<TimeDate> {
    val emptyTimes = ArrayList<TimeDate>()
    val downTime = when (timeDate.nameDay) {
      HalfWeekName.FRIDAY.nameDay -> {
        -6
      }

      HalfWeekName.THURSDAY.nameDay -> {
        -5
      }

      HalfWeekName.WEDNESDAY.nameDay -> {
        -4
      }

      HalfWeekName.TUESDAY.nameDay -> {
        -3
      }

      HalfWeekName.MONDAY.nameDay -> {
        -2
      }

      HalfWeekName.SUNDAY.nameDay -> {
        -1
      }

      HalfWeekName.SATURDAY.nameDay -> {
        0
      }

      else -> {
        1
      }
    }
    for (i in -1 downTo downTime) {
      emptyTimes.add(
        TimeDate(
          i,
          false,
          false,
          "",
          timeDate.yearNumber,
          timeDate.monthNumber,
          false,
          timeDate.monthName,
        ),
      )
    }
    return emptyTimes
  }

  private fun calculateDaySpaceEndMonth(timeDate: TimeDate): List<TimeDate> {
    val emptyTimes = ArrayList<TimeDate>()
    val downTime = when (timeDate.nameDay) {
      HalfWeekName.SATURDAY.nameDay -> {
        timeDate.dayNumber.plus(6)
      }

      HalfWeekName.SUNDAY.nameDay -> {
        timeDate.dayNumber.plus(5)
      }

      HalfWeekName.MONDAY.nameDay -> {
        timeDate.dayNumber.plus(4)
      }

      HalfWeekName.TUESDAY.nameDay -> {
        timeDate.dayNumber.plus(3)
      }

      HalfWeekName.WEDNESDAY.nameDay -> {
        timeDate.dayNumber.plus(2)
      }

      HalfWeekName.THURSDAY.nameDay -> {
        timeDate.dayNumber.plus(1)
      }

      HalfWeekName.FRIDAY.nameDay -> {
        timeDate.dayNumber.plus(0)
      }

      else -> {
        timeDate.dayNumber.plus(8)
      }
    }
    for (i in timeDate.dayNumber.plus(1)..downTime) {
      emptyTimes.add(
        TimeDate(
          i,
          false,
          false,
          "",
          timeDate.yearNumber,
          timeDate.monthNumber,
          false,
          timeDate.monthName,
        ),
      )
    }
    return emptyTimes
  }


  private suspend fun calculateDate() {
    withContext(defaultDispatcher) {
      val currentDate = TimeDate(
        persianData.shDay,
        false,
        true,
        calculateDay(persianData.dayName()),
        persianData.shYear,
        persianData.shMonth,
        monthName = persianData.monthName(),
        isChecked = true,
      )
      timeDao.insertTime(currentDate)
      var yearKabesi = calculateYearKabesi()
      calculateEmptyTime(yearKabesi)
      launch {
        val persianData = PersianDate()
        val dates = ArrayList<TimeDate>()
        for (year in currentDate.yearNumber.minus(2)..END_YEAR) {
          val isYearKabisy = yearKabesi.find { it == year } != null
          for (month in 1..12) {
            val dayNumber = if (month == 12) {
              if (isYearKabisy) {
                30
              } else
                29
            } else if (month in 7..11) 30 else 31
            for (day in 1..dayNumber) {
              persianData.initJalaliDate(year, month, day)
              val date = TimeDate(
                day,
                false,
                checkDayIsToday(year, month, day),
                calculateDay(persianData.dayName()),
                year,
                month,
                checkDayIsToday(year, month, day),
                monthName = persianData.monthName(),
              )
              dates.add(date)
            }
          }
          timeDao.insertAllTime(dates)
          dates.clear()
        }
      }
      launch {
        val persianData = PersianDate()
        val dates = ArrayList<TimeDate>()
        for (year in currentDate.yearNumber.minus(3) downTo FIRST_YEAR) {
          val isYearKabisy = yearKabesi.find { it == year } != null
          for (month in 1..12) {
            val dayNumber = if (month == 12) {
              if (isYearKabisy) {
                30
              } else
                29
            } else if (month in 7..11) 30 else 31
            for (day in 1..dayNumber) {
              persianData.initJalaliDate(year, month, day)
              val date = TimeDate(
                day,
                false,
                checkDayIsToday(year, month, day),
                calculateDay(persianData.dayName()),
                year,
                month,
                checkDayIsToday(year, month, day),
                monthName = persianData.monthName(),
                versionNumber = if (year == FIRST_YEAR) VERSION_TIME_DB else 0,
              )
              dates.add(date)
            }
          }
          timeDao.insertAllTime(dates)
          dates.clear()
        }
      }
    }
  }

  private suspend fun calculateYearKabesi(): List<Int> {
    return withContext(ioDispatcher) {
      val yearKabesi = ArrayList<Int>()
      var index = 4
      var differentBetweenYerKabesi = 7
      for (year in FIRST_YEAR..END_YEAR) {
        if (year >= FIRST_YEAR) {
          differentBetweenYerKabesi += 1
        }
        if ((((index % 4 == 0 && year != 1374) || year == FIRST_YEAR) && differentBetweenYerKabesi !in 29..32) || differentBetweenYerKabesi == 33) {
          if (differentBetweenYerKabesi == 33) {
            differentBetweenYerKabesi = 0
            index = 0
          }
          yearKabesi.add(year)
        }
        index += 1
      }
      yearKabesi
    }
  }

  private suspend fun calculateEmptyTime(yearKabesi: List<Int>) {
    withContext(ioDispatcher) {
      launch {
        val persianData = PersianDate()
        persianData.initJalaliDate(FIRST_YEAR, 1, 1)
        val dateStart = TimeDate(
          1,
          false,
          checkDayIsToday(FIRST_YEAR, 1, 1),
          calculateDay(persianData.dayName()),
          FIRST_YEAR,
          1,
          checkDayIsToday(FIRST_YEAR, 1, 1),
          monthName = persianData.monthName(),
        )
        val spaceStart =
          calculateDaySpaceStartMonth(dateStart).map { it }
        timeDao.insertAllTime(spaceStart)
      }
      launch {
        val persianData = PersianDate()
        val day = if (yearKabesi.find { it == END_YEAR } == null) 29 else 30
        persianData.initJalaliDate(END_YEAR, 12, day)
        val dateEnd = TimeDate(
          day,
          false,
          checkDayIsToday(END_YEAR, 12, day),
          calculateDay(persianData.dayName()),
          END_YEAR,
          12,
          checkDayIsToday(END_YEAR, 12, day),
          monthName = persianData.monthName(),
        )
        val spaceEnd = calculateDaySpaceEndMonth(dateEnd).map { it }
        timeDao.insertAllTime(spaceEnd)
      }
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

  private fun checkDayIsToday(year: Int, month: Int, day: Int): Boolean {
    if (year != currentTimeYear)
      return false
    if (month != currentTimeMonth)
      return false
    if (day != currentTimeDay)
      return false

    return true
  }

  override fun getCurrentNameDay(date: String, format: String): String {
    val persianDateFormat = PersianDateFormat()
    val da = persianDateFormat.parse(date, format)
    return persianData.dayName(da)
  }
}
