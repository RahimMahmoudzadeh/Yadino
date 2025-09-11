package com.rahim.yadino.home.data.repoImpl

import com.rahim.home.domain.model.CurrentDateDomainLayer
import com.rahim.home.domain.model.RoutineHomeDomainLayer
import com.rahim.home.domain.repo.HomeRepository
import com.rahim.yadino.Constants
import com.rahim.yadino.db.dao.routine.dao.RoutineDao
import com.rahim.yadino.home.data.mapper.toRoutineEntity
import com.rahim.yadino.home.data.mapper.toRoutineHomeDomainLayer
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

  override suspend fun addRoutine(routineModel: RoutineHomeDomainLayer) {
    sharedPreferencesRepository.setShowSampleRoutine(true)
    routineDao.addRoutine(routineModel.toRoutineEntity())
  }

  override suspend fun checkEqualRoutine(routineModel: RoutineHomeDomainLayer): RoutineHomeDomainLayer? {
    return routineDao.checkEqualRoutine(
      routineName = routineModel.name,
      routineExplanation = routineModel.explanation ?: "",
      routineDayName = routineModel.dayName,
      routineDayNumber = routineModel.dayNumber ?: 0,
      routineYearNumber = routineModel.yearNumber ?: 0,
      routineMonthNumber = routineModel.monthNumber ?: 0,
      routineTimeMilSecond = routineModel.timeInMillisecond ?: 0,
    )?.toRoutineHomeDomainLayer()
  }

  override fun convertDateToMilSecond(yearNumber: Int?, monthNumber: Int?, dayNumber: Int?, timeHours: String?): Long {
    val persianDateFormat = PersianDateFormat()
    val monthDate =
      if (monthNumber.toString().length == 1) "0$monthNumber" else monthNumber
    val dayNumber =
      if (dayNumber.toString().length == 1) "0$dayNumber" else dayNumber
    val hoursDate =
      if (timeHours.toString().length == 4) "0$timeHours" else timeHours
    val time =
      "$yearNumber-$monthDate-$dayNumber $hoursDate:00"
    val persianDate = persianDateFormat.parse(
      time,
      Constants.PATTERN_DATE,
    )
    return persianDate.time
  }

  override suspend fun getRoutineAlarmId(): Long {
    val existingAlarmIds = routineDao.getIdAlarms().toSet()
    var potentialId: Long
    do {
      potentialId = Random.Default.nextLong(1L, Long.MAX_VALUE)
    } while (existingAlarmIds.contains(potentialId))

    return potentialId
  }

  override fun getCurrentDate(): CurrentDateDomainLayer = CurrentDateDomainLayer(date = "$currentTimeYear/$currentTimeMonth/$currentTimeDay")

  override suspend fun removeRoutine(routineModel: RoutineHomeDomainLayer) {
    sharedPreferencesRepository.setShowSampleRoutine(true)
    routineDao.removeRoutine(routineModel.toRoutineEntity())
  }

  override suspend fun updateRoutine(routineModel: RoutineHomeDomainLayer) {
    sharedPreferencesRepository.setShowSampleRoutine(true)
    routineDao.addRoutine(routineModel.toRoutineEntity())
  }

  override suspend fun getRoutine(id: Int): RoutineHomeDomainLayer = routineDao.getRoutine(id).toRoutineHomeDomainLayer()
  override suspend fun checkedRoutine(routineModel: RoutineHomeDomainLayer) {
    Timber.Forest.tag("routineViewModel").d("checkedRoutine")
    sharedPreferencesRepository.setShowSampleRoutine(true)
    routineDao.addRoutine(routineModel.toRoutineEntity())
  }

  override fun getTodayRoutines(): Flow<List<RoutineHomeDomainLayer>> =
    routineDao.getRoutinesByDate(monthNumber = currentTimeMonth, dayNumber = currentTimeDay, yearNumber = currentTimeYear).map { it.map { it.toRoutineHomeDomainLayer() } }

  override fun searchTodayRoutine(name: String): Flow<List<RoutineHomeDomainLayer>> =
    routineDao.searchRoutine(nameRoutine = name, monthNumber = currentTimeMonth, dayNumber = currentTimeDay, yearNumber = currentTimeYear).map { it.map { it.toRoutineHomeDomainLayer() } }
}
