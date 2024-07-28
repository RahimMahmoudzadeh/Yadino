package com.rahim.yadino.routine_repository

import com.rahim.yadino.base.di.IODispatcher
import com.rahim.yadino.base.enums.error.ErrorMessageCode
import com.rahim.yadino.base.sharedPreferences.SharedPreferencesCustom
import com.rahim.yadino.routine.RepositoryRoutine
import com.rahim.yadino.routine.modle.Resource
import com.rahim.yadino.routine.modle.Routine.Routine
import com.rahim.yadino.routine_local.dao.RoutineDao
import com.rahim.yadino.routine_repository.mapper.toLocalRoutineDto
import com.rahim.yadino.routine_repository.mapper.toRoutine
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import saman.zamani.persiandate.PersianDate
import saman.zamani.persiandate.PersianDateFormat
import javax.inject.Inject
import kotlin.random.Random

private const val ROUTINE_LEFT_SAMPLE = "من یک روتین تستی هستم لطفا من را به چپ بکشید"
private const val ROUTINE_RIGHT_SAMPLE = "من یک روتین تستی هستم لطفا من را به راست بکشید"

class RoutineRepositoryImpl @Inject constructor(
    private val routineDao: RoutineDao,
    private val sharedPreferencesCustom: SharedPreferencesCustom,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : RepositoryRoutine {
    private val persianData = PersianDate()
    private val currentTimeDay = persianData.shDay
    private val currentTimeMonth = persianData.shMonth
    private val currentTimeYer = persianData.shYear

    override suspend fun addSampleRoutine() {
        delay(500)
        if (sharedPreferencesCustom.isShowSampleRoutine()) {
            routineDao.removeSampleRoutine()
            return
        }

        (0..1).forEachIndexed { index, it ->
            val routine = Routine(
                "تست${index.plus(1)}",
                0,
                currentTimeDay.toString(),
                currentTimeDay,
                currentTimeMonth,
                currentTimeYer,
                "12:00",
                false,
                explanation = if (index == 1) ROUTINE_LEFT_SAMPLE else ROUTINE_RIGHT_SAMPLE,
                isSample = true,
                idAlarm = index.plus(1).toLong(),
                timeInMillisecond = persianData.time,
                id = index,
            )
            routineDao.addRoutine(routine.toLocalRoutineDto())
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
                it.apply {
                    idAlarm = idRandom.toLong()
                }
                routineDao.updateRoutine(it)
            }
        }
    }

    private suspend fun getIdAlarmsNotNull(): List<Long> {
        val idAlarms = routineDao.getIdAlarms()
        val idNotNull = ArrayList<Long>()
        if (idAlarms.isNotEmpty())
            idNotNull.addAll(idAlarms.filter {
                it != null
            })
        return idNotNull
    }

    override suspend fun checkEdAllRoutinePastTime() {
        withContext(ioDispatcher) {
            val routines = routineDao.getRoutines().filter { it.timeInMillisecond != null }
            val pastTimeRoutine = routines.filter {
                (it.timeInMillisecond ?: 0) < System.currentTimeMillis() && !it.isSample
            }
            pastTimeRoutine.forEach {
                routineDao.updateRoutine(it.apply {
                    isChecked = true
                })
            }
        }
    }

    override suspend fun getAllRoutine(): List<Routine> =
        routineDao.getRoutines().map { list -> list.toRoutine() }

    override fun addRoutine(routine: Routine): Flow<com.rahim.yadino.routine.modle.Resource<Routine?>> =
        flow<com.rahim.yadino.routine.modle.Resource<Routine?>> {
            emit(com.rahim.yadino.routine.modle.Resource.Loading())
            routine.apply {
                idAlarm = getRoutineAlarmId()
                colorTask = 0
                timeInMillisecond = convertDateToMilSecond(
                    this.yerNumber,
                    this.monthNumber,
                    this.dayNumber,
                    this.timeHours
                )
            }
            val equalRoutine = routineDao.checkEqualRoutine(
                routineName = routine.name,
                routineExplanation = routine.explanation ?: "",
                routineDayName = routine.dayName,
                routineDayNumber = routine.dayNumber ?: 0,
                routineYearNumber = routine.yerNumber ?: 0,
                routineMonthNumber = routine.monthNumber ?: 0,
                routineTimeMilSecond = routine.timeInMillisecond ?: 0,

                )
            if (equalRoutine == null) {
                runCatching {
                    routineDao.addRoutine(routine.toLocalRoutineDto())
                }.onSuccess {
                    emit(Resource.Success(routine))
                }.onFailure {
                    emit(Resource.Error(ErrorMessageCode.EQUAL_ROUTINE_MESSAGE))
                }
            } else {
                emit(Resource.Error(ErrorMessageCode.EQUAL_ROUTINE_MESSAGE))
            }
        }.flowOn(ioDispatcher)

    private fun convertDateToMilSecond(
        yerNumber: Int?,
        monthNumber: Int?,
        dayNumber: Int?,
        timeHours: String?
    ): Long {
        val persianDateFormat = PersianDateFormat()
        val monthDate =
            if (monthNumber.toString().length == 1) "0${monthNumber.toString()}" else monthNumber
        val dayNumber =
            if (dayNumber.toString().length == 1) "0${dayNumber.toString()}" else dayNumber
        val hoursDate =
            if (timeHours.toString().length == 4) "0${timeHours.toString()}" else timeHours
        val time =
            "${yerNumber}-${monthDate}-${dayNumber} ${hoursDate}:00"
        val persianDate = persianDateFormat.parse(
            time,
            "yyyy-MM-dd HH:mm:ss a"
        )
        return persianDate.time
    }

    private suspend fun getRoutineAlarmId(): Long {
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
        return routineDao.removeRoutine(routine.toLocalRoutineDto())
    }

    override suspend fun removeAllRoutine(nameMonth: Int?, dayNumber: Int?, yerNumber: Int?) {
        routineDao.removeAllRoutine(nameMonth, dayNumber, yerNumber)
    }

    override suspend fun updateRoutine(routine: Routine): Flow<com.rahim.yadino.routine.modle.Resource<Routine?>> =
        flow {
            routine.apply {
                timeInMillisecond = convertDateToMilSecond(
                    this.yerNumber,
                    this.monthNumber,
                    this.dayNumber,
                    this.timeHours
                )
            }
            val equalRoutine = routineDao.checkEqualRoutine(
                routineName = routine.name,
                routineExplanation = routine.explanation ?: "",
                routineDayName = routine.dayName,
                routineDayNumber = routine.dayNumber ?: 0,
                routineYearNumber = routine.yerNumber ?: 0,
                routineMonthNumber = routine.monthNumber ?: 0,
                routineTimeMilSecond = routine.timeInMillisecond ?: 0,
            )
            if (equalRoutine != null) {
                emit(Resource.Error(ErrorMessageCode.EQUAL_ROUTINE_MESSAGE))
            } else {
                runCatching {
                    routineDao.updateRoutine(routine.toLocalRoutineDto())
                }.onSuccess {
                    emit(Resource.Success(routine))
                }.onFailure {
                    emit(Resource.Error(ErrorMessageCode.EQUAL_ROUTINE_MESSAGE))
                }
            }
        }

    override suspend fun getRoutine(id: Int): Routine = routineDao.getRoutine(id).toRoutine()
    override suspend fun checkedRoutine(routine: Routine) {
        routineDao.updateRoutine(routine.toLocalRoutineDto())
    }

    override fun getRoutines(
        monthNumber: Int, numberDay: Int, yerNumber: Int
    ): Flow<List<Routine>> =
        routineDao.getRoutines(monthNumber, numberDay, yerNumber)
            .map { list -> list.map { it.toRoutine() } }
            .distinctUntilChangedBy { it.map { it.isChecked } }

    override fun searchRoutine(
        name: String, monthNumber: Int?, dayNumber: Int?
    ): Flow<List<Routine>> = routineDao.searchRoutine(name, monthNumber, dayNumber)
        .map { list -> list.map { it.toRoutine() } }
}