package com.rahim.yadino.home.data.repoImpl

import androidx.core.net.ParseException
import com.rahim.home.domain.model.CurrentDate
import com.rahim.home.domain.model.Routine
import com.rahim.home.domain.repo.HomeRepository
import com.rahim.yadino.Constants
import com.rahim.yadino.db.routine.dao.RoutineDao
import com.rahim.yadino.home.data.mapper.toRoutineEntity
import com.rahim.yadino.home.data.mapper.toRoutine
import com.rahim.yadino.sharedPreferences.repo.SharedPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import saman.zamani.persiandate.PersianDate
import saman.zamani.persiandate.PersianDateFormat
import timber.log.Timber
import javax.inject.Inject
import kotlin.random.Random

class HomeRepositoryImpl @Inject constructor(
  private val routineDao: RoutineDao,
  private val sharedPreferencesRepository: SharedPreferencesRepository,
) : HomeRepository {

  private val persianData = PersianDate()
  private val currentTimeDay = persianData.shDay
  private val currentTimeMonth = persianData.shMonth
  private val currentTimeYear = persianData.shYear

  override suspend fun addRoutine(routineModel: Routine) {
    sharedPreferencesRepository.setShowSampleRoutine(true)
    routineDao.addRoutine(routineModel.toRoutineEntity())
  }

  override suspend fun checkEqualRoutine(routineModel: Routine): Routine? {
    return routineDao.checkEqualRoutine(
      routineName = routineModel.name,
      routineExplanation = routineModel.explanation ?: "",
      routineDayName = routineModel.dayName,
      routineDayNumber = routineModel.dayNumber ?: 0,
      routineYearNumber = routineModel.yearNumber ?: 0,
      routineMonthNumber = routineModel.monthNumber ?: 0,
      routineTimeMilSecond = routineModel.timeInMillisecond ?: 0,
    )?.toRoutine()
  }

  override fun convertDateToMilSecond(year: Int?, month: Int?, day: Int?, hours: String?): Long {
    if (year == null || month == null || day == null || hours == null) {
      throw IllegalArgumentException("Date components cannot be null")
    }
    val persianDateFormat = PersianDateFormat()
    val formattedMonth = String.format("%02d", month)
    val formattedDay = String.format("%02d", day)

    val formattedHours = if (hours.length == 4) {
      "0$hours"
    } else if (hours.length == 5 && hours[2] == ':') {
      hours
    } else {
      throw IllegalArgumentException("Invalid timeHours format: $hours. Expected HHMM or HH:MM")
    }

    val formattedDateTime = "$year-$formattedMonth-$formattedDay $formattedHours:00"

    return try {
      val persianDate = persianDateFormat.parse(
        formattedDateTime,
        Constants.PATTERN_DATE, // Ensure this pattern matches the timeString format
      )
      persianDate.time
    } catch (e: ParseException) {
      Timber.tag("HomeRepositoryImpl").d("Failed to parse date string: $formattedDateTime with pattern ${Constants.PATTERN_DATE}. Error: ${e.message}")
      0L
    }
  }

  override suspend fun getRoutineAlarmId(): Long {
    val existingAlarmIds = routineDao.getIdAlarms().toSet()
    var potentialId: Long
    do {
      potentialId = Random.Default.nextLong(1L, Long.MAX_VALUE)
    } while (existingAlarmIds.contains(potentialId))

    return potentialId
  }

  override fun getCurrentDate(): CurrentDate = CurrentDate(date = "$currentTimeYear/$currentTimeMonth/$currentTimeDay")

  override suspend fun removeRoutine(routineModel: Routine) {
    sharedPreferencesRepository.setShowSampleRoutine(true)
    routineDao.removeRoutine(routineModel.toRoutineEntity())
  }

  override suspend fun updateRoutine(routineModel: Routine) {
    sharedPreferencesRepository.setShowSampleRoutine(true)
    routineDao.addRoutine(routineModel.toRoutineEntity())
  }

  override suspend fun getRoutine(id: Int): Routine = routineDao.getRoutine(id).toRoutine()
  override suspend fun checkedRoutine(routineModel: Routine) {
    Timber.Forest.tag("routineViewModel").d("checkedRoutine")
    sharedPreferencesRepository.setShowSampleRoutine(true)
    routineDao.addRoutine(routineModel.toRoutineEntity())
  }

  override fun getTodayRoutines(): Flow<List<Routine>> =
    routineDao.getRoutinesByDate(monthNumber = currentTimeMonth, dayNumber = currentTimeDay, yearNumber = currentTimeYear).map { it.map { it.toRoutine() } }

  override fun searchTodayRoutine(name: String): Flow<List<Routine>> =
    routineDao.searchRoutine(nameRoutine = name, monthNumber = currentTimeMonth, dayNumber = currentTimeDay, yearNumber = currentTimeYear).map { it.map { it.toRoutine() } }
}
