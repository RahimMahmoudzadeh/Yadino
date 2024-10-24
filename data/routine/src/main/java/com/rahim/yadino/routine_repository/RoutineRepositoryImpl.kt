package com.rahim.yadino.routine_repository

import com.rahim.yadino.Constants.PATTERN_DATE
import com.rahim.yadino.enums.error.ErrorMessageCode
import com.rahim.yadino.routine.RepositoryRoutine
import com.rahim.yadino.Resource
import com.rahim.yadino.model.RoutineModel
import com.rahim.yadino.db.dao.RoutineDao
import com.rahim.yadino.sharedPreferences.SharedPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.withContext
import saman.zamani.persiandate.PersianDate
import saman.zamani.persiandate.PersianDateFormat
import javax.inject.Inject
import kotlin.random.Random

private const val ROUTINE_LEFT_SAMPLE = "من یک روتین تستی هستم لطفا من را به چپ بکشید"
private const val ROUTINE_RIGHT_SAMPLE = "من یک روتین تستی هستم لطفا من را به راست بکشید"

class RoutineRepositoryImpl @Inject constructor(
  private val routineDao: RoutineDao,
  private val sharedPreferencesRepository: SharedPreferencesRepository,
) : RepositoryRoutine {
  private val persianData = PersianDate()
  private val currentTimeDay = persianData.shDay
  private val currentTimeMonth = persianData.shMonth
  private val currentTimeYear = persianData.shYear

  override suspend fun addSampleRoutine() {
    delay(500)
    if (sharedPreferencesRepository.isShowSampleRoutine()) {
      routineDao.removeSampleRoutine()
      return
    }

    (0..1).forEachIndexed { index, it ->
      val routineModel = RoutineModel(
        "تست${index.plus(1)}",
        0,
        currentTimeDay.toString(),
        currentTimeDay,
        currentTimeMonth,
        currentTimeYear,
        "12:00",
        false,
        explanation = if (index == 1) ROUTINE_LEFT_SAMPLE else ROUTINE_RIGHT_SAMPLE,
        isSample = true,
        idAlarm = index.plus(1).toLong(),
        timeInMillisecond = persianData.time,
        id = index,
      )
      routineDao.addRoutine(routineModel)
    }
  }

  override suspend fun changeRoutineId() = withContext(Dispatchers.IO) {
    val routines = routineDao.getRoutines()

    routines.forEach {
      if (it.idAlarm == null) {
        var idRandom = Random.nextInt(0, 10000000)
        var equalIdAlarm = getIdAlarmsNotNull().find { it == idRandom.toLong() }
        while (equalIdAlarm != null) {
          idRandom = Random.nextInt(0, 10000000)
          equalIdAlarm = getIdAlarmsNotNull().find { it == idRandom.toLong() }
        }
        routineDao.updateRoutine(it.copy(idAlarm = idRandom.toLong()))
      }
    }
  }

  private suspend fun getIdAlarmsNotNull(): List<Long> {
    val idAlarms = routineDao.getIdAlarms()
    val idNotNull = ArrayList<Long>()
    if (idAlarms.isNotEmpty())
      idNotNull.addAll(
        idAlarms.filter {
          it != null
        },
      )
    return idNotNull
  }

  override suspend fun checkedAllRoutinePastTime() {
    routineDao.updateRoutinesPastTime(System.currentTimeMillis())
  }

  override suspend fun getAllRoutine(): List<RoutineModel> =
    routineDao.getRoutines()

  override suspend fun addRoutine(routineModel: RoutineModel) {
    sharedPreferencesRepository.setShowSampleRoutine(true)
    routineDao.addRoutine(routineModel)
  }

  override suspend fun checkEqualRoutine(routineModel: RoutineModel): RoutineModel? {
    return routineDao.checkEqualRoutine(
      routineName = routineModel.name,
      routineExplanation = routineModel.explanation ?: "",
      routineDayName = routineModel.dayName,
      routineDayNumber = routineModel.dayNumber ?: 0,
      routineYearNumber = routineModel.yearNumber ?: 0,
      routineMonthNumber = routineModel.monthNumber ?: 0,
      routineTimeMilSecond = routineModel.timeInMillisecond ?: 0,
    )
  }

  override fun convertDateToMilSecond(
    yearNumber: Int?,
    monthNumber: Int?,
    dayNumber: Int?,
    timeHours: String?,
  ): Long {
    val persianDateFormat = PersianDateFormat()
    val monthDate =
      if (monthNumber.toString().length == 1) "0${monthNumber.toString()}" else monthNumber
    val dayNumber =
      if (dayNumber.toString().length == 1) "0${dayNumber.toString()}" else dayNumber
    val hoursDate =
      if (timeHours.toString().length == 4) "0${timeHours.toString()}" else timeHours
    val time =
      "${yearNumber}-${monthDate}-${dayNumber} ${hoursDate}:00"
    val persianDate = persianDateFormat.parse(
      time,
      PATTERN_DATE,
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

  override suspend fun removeRoutine(routineModel: RoutineModel): Int {
    sharedPreferencesRepository.setShowSampleRoutine(true)
    return routineDao.removeRoutine(routineModel)
  }

  override suspend fun removeAllRoutine(nameMonth: Int?, dayNumber: Int?, yearNumber: Int?) {
    routineDao.removeAllRoutine(nameMonth, dayNumber, yearNumber)
  }

  override fun updateRoutine(routineModel: RoutineModel): Flow<Resource<RoutineModel?>> =
    flow {
      sharedPreferencesRepository.setShowSampleRoutine(true)
      val updateRoutine = routineModel.copy(
        timeInMillisecond = convertDateToMilSecond(
          routineModel.yearNumber,
          routineModel.monthNumber,
          routineModel.dayNumber,
          routineModel.timeHours,
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
            routineDao.updateRoutine(this)
          }.onSuccess {
            emit(Resource.Success(this))
          }.onFailure {
            emit(Resource.Error(ErrorMessageCode.EQUAL_ROUTINE_MESSAGE))
          }
        }
      }
    }

  override suspend fun getRoutine(id: Int): RoutineModel = routineDao.getRoutine(id)
  override suspend fun checkedRoutine(routineModel: RoutineModel) {
    sharedPreferencesRepository.setShowSampleRoutine(true)
    routineDao.updateRoutine(routineModel)
  }

  private var lastYearNumber = 0
  private var lastMonthNumber = 0
  private var lastDayNumber = 0
  override fun getRoutines(
    monthNumber: Int, numberDay: Int, yearNumber: Int,
  ): Flow<List<RoutineModel>> = flow {
    lastYearNumber = yearNumber
    lastMonthNumber = monthNumber
    lastDayNumber = numberDay
    routineDao.getRoutines(monthNumber, numberDay, yearNumber).takeWhile {
      if (it.isEmpty()) {
        true
      } else {
        val firstRoutine = it.first()
        (firstRoutine.monthNumber == lastMonthNumber && firstRoutine.dayNumber == lastDayNumber && firstRoutine.yearNumber == lastYearNumber)
      }
    }.collect {
      emit(it)
    }
  }


  override suspend fun searchRoutine(
    name: String, yearNumber: Int?, monthNumber: Int?, dayNumber: Int?,
  ): List<RoutineModel> = routineDao.searchRoutine(name, monthNumber, dayNumber)

  override fun haveAlarm(): Flow<Boolean> = routineDao.haveAlarm()
}
