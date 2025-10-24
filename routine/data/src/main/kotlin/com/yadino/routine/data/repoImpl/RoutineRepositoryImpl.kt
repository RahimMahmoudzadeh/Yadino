package com.yadino.routine.data.repoImpl

import com.rahim.yadino.Constants
import com.rahim.yadino.base.Resource
import com.rahim.yadino.db.routine.dao.RoutineDao
import com.rahim.yadino.db.routine.model.RoutineEntity
import com.rahim.yadino.enums.RoutineExplanation
import com.rahim.yadino.enums.error.ErrorMessageCode
import com.rahim.yadino.sharedPreferences.repo.SharedPreferencesRepository
import com.yadino.routine.data.mapper.toRoutineEntity
import com.yadino.routine.data.mapper.toRoutineModelDomainLayer
import com.yadino.routine.domain.repo.RoutineRepository
import com.yadino.routine.domain.model.Routine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import saman.zamani.persiandate.PersianDate
import saman.zamani.persiandate.PersianDateFormat
import timber.log.Timber
import kotlin.random.Random

class RoutineRepositoryImpl(
    private val routineDao: RoutineDao,
    private val sharedPreferencesRepository: SharedPreferencesRepository,
) : RoutineRepository {
  private val persianData = PersianDate()

  override suspend fun addSampleRoutine() {
      delay(500)
    if (sharedPreferencesRepository.isShowSampleRoutine().first()) {
      routineDao.removeSampleRoutine()
      return
    }

    (0..1).forEachIndexed { index, it ->
      val routineEntity = RoutineEntity(
          name = "تست${index.plus(1)}",
          colorTask = 0,
          dayName = persianData.dayName(),
          dayNumber = persianData.shDay,
          monthNumber = persianData.shMonth,
          yearNumber = persianData.shYear,
          timeHours = "12:00",
          isChecked = false,
          explanation = if (index == 1) RoutineExplanation.ROUTINE_LEFT_SAMPLE.explanation else RoutineExplanation.ROUTINE_RIGHT_SAMPLE.explanation,
          isSample = true,
          idAlarm = index.plus(1).toLong(),
          timeInMillisecond = persianData.time,
          id = index,
      )
      routineDao.addRoutine(routineEntity)
    }
  }

  override suspend fun changeRoutineId() = withContext(Dispatchers.IO) {
      val routines = routineDao.getRoutinesByDate()

      routines.forEach {
          if (it.idAlarm == null) {
              var idRandom = Random.nextInt(0, 10000000)
              var equalIdAlarm = getIdAlarmsNotNull().find { it == idRandom.toLong() }
              while (equalIdAlarm != null) {
                  idRandom = Random.nextInt(0, 10000000)
                  equalIdAlarm = getIdAlarmsNotNull().find { it == idRandom.toLong() }
              }
              routineDao.addRoutine(it.copy(idAlarm = idRandom.toLong()))
          }
      }
  }

  private suspend fun getIdAlarmsNotNull(): List<Long> {
    val idAlarms = routineDao.getIdAlarms()
    val idNotNull = ArrayList<Long>()
    if (idAlarms.isNotEmpty()) {
      idNotNull.addAll(
        idAlarms.filter {
          it != null
        },
      )
    }
    return idNotNull
  }

  override suspend fun checkedAllRoutinePastTime() {
    routineDao.updateRoutinesPastTime(System.currentTimeMillis())
  }

  override suspend fun getAllRoutine(): List<Routine> = routineDao.getRoutinesByDate().map { it.toRoutineModelDomainLayer() }

  override suspend fun addRoutine(routine: Routine) {
    sharedPreferencesRepository.setShowSampleRoutine(true)
    routineDao.addRoutine(routine.toRoutineEntity())
  }

  override suspend fun checkEqualRoutine(routine: Routine): Routine? {
    return routineDao.checkEqualRoutine(
      routineName = routine.name,
      routineExplanation = routine.explanation ?: "",
      routineDayName = routine.dayName,
      routineDayNumber = routine.dayNumber ?: 0,
      routineYearNumber = routine.yearNumber ?: 0,
      routineMonthNumber = routine.monthNumber ?: 0,
      routineTimeMilSecond = routine.timeInMillisecond ?: 0,
    )?.toRoutineModelDomainLayer()
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
    val idAlarms = routineDao.getIdAlarms()
    var idRandom = Random.nextInt(0, 10000000)
    var equalIdAlarm = idAlarms.find { it == idRandom.toLong() }
    while (equalIdAlarm != null) {
      idRandom = Random.nextInt(0, 10000000)
      equalIdAlarm = idAlarms.find { it == idRandom.toLong() }
    }
    return idRandom.toLong()
  }

  override suspend fun removeRoutine(routine: Routine): Int {
    sharedPreferencesRepository.setShowSampleRoutine(true)
    return routineDao.removeRoutine(routine.toRoutineEntity())
  }

  override suspend fun removeAllRoutine(nameMonth: Int?, dayNumber: Int?, yearNumber: Int?) {
    routineDao.removeAllRoutine(nameMonth, dayNumber, yearNumber)
  }

  override fun updateRoutine(routine: Routine): Flow<Resource<Routine, ErrorMessageCode>> = flow {
      sharedPreferencesRepository.setShowSampleRoutine(true)
      val updateRoutine = routine.copy(
          timeInMillisecond = convertDateToMilSecond(
              routine.yearNumber,
              routine.monthNumber,
              routine.dayNumber,
              routine.timeHours,
          ),
      )
      updateRoutine.run {
          val equalRoutine = routineDao.checkEqualRoutine(
              routineName = name,
              routineExplanation = explanation ?: "",
              routineDayName = dayName,
              routineDayNumber = dayNumber ?: 0,
              routineYearNumber = yearNumber ?: 0,
              routineMonthNumber = monthNumber ?: 0,
              routineTimeMilSecond = timeInMillisecond ?: 0,
          )
          if (equalRoutine != null) {
              emit(Resource.Error(ErrorMessageCode.EQUAL_ROUTINE_MESSAGE))
          } else {
              runCatching {
                  routineDao.addRoutine(this.toRoutineEntity())
              }.onSuccess {
                  emit(Resource.Success(this))
              }.onFailure {
                  emit(Resource.Error(ErrorMessageCode.EQUAL_ROUTINE_MESSAGE))
              }
          }
      }
  }

  override suspend fun getRoutine(id: Int): Routine = routineDao.getRoutine(id).toRoutineModelDomainLayer()
  override suspend fun checkedRoutine(routine: Routine) {
    Timber.tag("routineViewModel").d("checkedRoutine")
    sharedPreferencesRepository.setShowSampleRoutine(true)
    routineDao.addRoutine(routine.toRoutineEntity())
  }

  override fun getRoutines(monthNumber: Int, numberDay: Int, yearNumber: Int): Flow<List<Routine>> = routineDao.getRoutinesByDate(monthNumber, numberDay, yearNumber).map { it.map { it.toRoutineModelDomainLayer() } }

  override fun searchRoutine(name: String, yearNumber: Int?, monthNumber: Int?, dayNumber: Int?): Flow<List<Routine>> = routineDao.searchRoutine(nameRoutine = name, monthNumber = monthNumber, dayNumber = dayNumber, yearNumber = yearNumber).map { it.map { it.toRoutineModelDomainLayer() } }

  override fun haveAlarm(): Flow<Boolean> = routineDao.haveAlarm()
}
