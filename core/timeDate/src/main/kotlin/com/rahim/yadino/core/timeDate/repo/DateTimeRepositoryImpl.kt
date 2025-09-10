package com.rahim.yadino.core.timeDate.repo

import com.rahim.yadino.Constants
import com.rahim.yadino.core.timeDate.mapper.toTimeDate
import com.rahim.yadino.core.timeDate.model.TimeDateModel
import com.rahim.yadino.db.dao.dateTime.dao.TimeDao
import com.rahim.yadino.db.dao.dateTime.model.TimeDateEntity
import com.rahim.yadino.di.DefaultDispatcher
import com.rahim.yadino.di.IODispatcher
import com.rahim.yadino.enums.HalfWeekName
import com.rahim.yadino.enums.WeekName
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
      times.find { it.yearNumber == Constants.FIRST_YEAR && it.versionNumber == Constants.VERSION_TIME_DB }
    if (firstTime != null) {
      return
    }
    if (times.isNotEmpty()) {
      timeDao.deleteAllTimes()
    }

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

  override fun getTimes(): Flow<List<TimeDateModel>> = timeDao.getAllTimeFlow().map { it.map { it.toTimeDate() } }

  override suspend fun getTimesMonth(yearNumber: Int, monthNumber: Int): List<TimeDateModel> {
    val timesDb = timeDao.getSpecificMonthFromYear(monthNumber, yearNumber)
    return if (yearNumber != Constants.FIRST_YEAR || yearNumber != Constants.END_YEAR) {
      if (timesDb.isNotEmpty()) {
        val times = ArrayList<TimeDateEntity>()
        val spaceStart = calculateDaySpaceStartMonth(timesDb.first())
        val spaceEnd = calculateDaySpaceEndMonth(timesDb.last())
        times.addAll(spaceStart)
        times.addAll(timesDb)
        times.addAll(spaceEnd)
        times.map { it.toTimeDate() }
      } else {
        emptyList()
      }
    } else {
      timesDb.map { it.toTimeDate() }
    }
  }

  private fun calculateDaySpaceStartMonth(timeDateEntity: TimeDateEntity): List<TimeDateEntity> {
    val emptyTimes = ArrayList<TimeDateEntity>()
    val downTime = when (timeDateEntity.nameDay) {
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
        TimeDateEntity(
          i,
          false,
          false,
          "",
          timeDateEntity.yearNumber,
          timeDateEntity.monthNumber,
          false,
          timeDateEntity.monthName,
        ),
      )
    }
    return emptyTimes
  }

  private fun calculateDaySpaceEndMonth(timeDateEntity: TimeDateEntity): List<TimeDateEntity> {
    val emptyTimes = ArrayList<TimeDateEntity>()
    val downTime = when (timeDateEntity.nameDay) {
      HalfWeekName.SATURDAY.nameDay -> {
        timeDateEntity.dayNumber.plus(6)
      }

      HalfWeekName.SUNDAY.nameDay -> {
        timeDateEntity.dayNumber.plus(5)
      }

      HalfWeekName.MONDAY.nameDay -> {
        timeDateEntity.dayNumber.plus(4)
      }

      HalfWeekName.TUESDAY.nameDay -> {
        timeDateEntity.dayNumber.plus(3)
      }

      HalfWeekName.WEDNESDAY.nameDay -> {
        timeDateEntity.dayNumber.plus(2)
      }

      HalfWeekName.THURSDAY.nameDay -> {
        timeDateEntity.dayNumber.plus(1)
      }

      HalfWeekName.FRIDAY.nameDay -> {
        timeDateEntity.dayNumber.plus(0)
      }

      else -> {
        timeDateEntity.dayNumber.plus(8)
      }
    }
    for (i in timeDateEntity.dayNumber.plus(1)..downTime) {
      emptyTimes.add(
        TimeDateEntity(
          i,
          false,
          false,
          "",
          timeDateEntity.yearNumber,
          timeDateEntity.monthNumber,
          false,
          timeDateEntity.monthName,
        ),
      )
    }
    return emptyTimes
  }

  private suspend fun calculateDate() {
      withContext(defaultDispatcher) {
          val currentDate = TimeDateEntity(
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
              val dates = ArrayList<TimeDateEntity>()
              for (year in currentDate.yearNumber.minus(2)..Constants.END_YEAR) {
                  val isYearKabisy = yearKabesi.find { it == year } != null
                  for (month in 1..12) {
                      val dayNumber = if (month == 12) {
                          if (isYearKabisy) {
                              30
                          } else {
                              29
                          }
                      } else if (month in 7..11) 30 else 31
                      for (day in 1..dayNumber) {
                          persianData.initJalaliDate(year, month, day)
                          val date = TimeDateEntity(
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
              val dates = ArrayList<TimeDateEntity>()
              for (year in currentDate.yearNumber.minus(3) downTo Constants.FIRST_YEAR) {
                  val isYearKabisy = yearKabesi.find { it == year } != null
                  for (month in 1..12) {
                      val dayNumber = if (month == 12) {
                          if (isYearKabisy) {
                              30
                          } else {
                              29
                          }
                      } else if (month in 7..11) 30 else 31
                      for (day in 1..dayNumber) {
                          persianData.initJalaliDate(year, month, day)
                          val date = TimeDateEntity(
                              day,
                              false,
                              checkDayIsToday(year, month, day),
                              calculateDay(persianData.dayName()),
                              year,
                              month,
                              checkDayIsToday(year, month, day),
                              monthName = persianData.monthName(),
                              versionNumber = if (year == Constants.FIRST_YEAR) Constants.VERSION_TIME_DB else 0,
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
        for (year in Constants.FIRST_YEAR..Constants.END_YEAR) {
            if (year >= Constants.FIRST_YEAR) {
                differentBetweenYerKabesi += 1
            }
            if ((((index % 4 == 0 && year != 1374) || year == Constants.FIRST_YEAR) && differentBetweenYerKabesi !in 29..32) || differentBetweenYerKabesi == 33) {
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
              persianData.initJalaliDate(Constants.FIRST_YEAR, 1, 1)
              val dateStart = TimeDateEntity(
                  1,
                  false,
                  checkDayIsToday(Constants.FIRST_YEAR, 1, 1),
                  calculateDay(persianData.dayName()),
                  Constants.FIRST_YEAR,
                  1,
                  checkDayIsToday(Constants.FIRST_YEAR, 1, 1),
                  monthName = persianData.monthName(),
              )
              val spaceStart =
                  calculateDaySpaceStartMonth(dateStart).map { it }
              timeDao.insertAllTime(spaceStart)
          }
          launch {
              val persianData = PersianDate()
              val day = if (yearKabesi.find { it == Constants.END_YEAR } == null) 29 else 30
              persianData.initJalaliDate(Constants.END_YEAR, 12, day)
              val dateEnd = TimeDateEntity(
                  day,
                  false,
                  checkDayIsToday(Constants.END_YEAR, 12, day),
                  calculateDay(persianData.dayName()),
                  Constants.END_YEAR,
                  12,
                  checkDayIsToday(Constants.END_YEAR, 12, day),
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
    if (year != currentTimeYear) {
      return false
    }
    if (month != currentTimeMonth) {
      return false
    }
    if (day != currentTimeDay) {
      return false
    }

    return true
  }

  override fun getCurrentNameDay(date: String, format: String): String {
    val persianDateFormat = PersianDateFormat()
    val da = persianDateFormat.parse(date, format)
    return persianData.dayName(da)
  }
}
