package com.rahim.yadino.routine_repository

import com.rahim.yadino.base.di.IODispatcher
import com.rahim.yadino.base.enums.error.ErrorMessageCode
import com.rahim.yadino.base.sharedPreferences.SharedPreferencesCustom
import com.rahim.yadino.routine.RepositoryRoutine
import com.rahim.yadino.base.Resource
import com.rahim.yadino.base.db.model.RoutineModel
import com.rahim.yadino.base.db.dao.RoutineDao
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
            val routineModel = RoutineModel(
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

    override suspend fun getAllRoutine(): List<RoutineModel> =
        routineDao.getRoutines().map { list -> list }

    override fun addRoutine(routineModel: RoutineModel): Flow<Resource<RoutineModel?>> =
        flow<Resource<RoutineModel?>> {
            emit(Resource.Loading())
            runCatching {
                routineDao.addRoutine(routineModel)
            }.onSuccess {
                emit(Resource.Success(routineModel))
            }.onFailure {
                emit(Resource.Error(ErrorMessageCode.EQUAL_ROUTINE_MESSAGE))
            }

        }.flowOn(ioDispatcher)

    override suspend fun checkEqualRoutine(routineModel: RoutineModel): RoutineModel? {
        return routineDao.checkEqualRoutine(
            routineName = routineModel.name,
            routineExplanation = routineModel.explanation ?: "",
            routineDayName = routineModel.dayName,
            routineDayNumber = routineModel.dayNumber ?: 0,
            routineYearNumber = routineModel.yerNumber ?: 0,
            routineMonthNumber = routineModel.monthNumber ?: 0,
            routineTimeMilSecond = routineModel.timeInMillisecond ?: 0,
        )
    }

    override fun convertDateToMilSecond(
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
        return routineDao.removeRoutine(routineModel)
    }

    override suspend fun removeAllRoutine(nameMonth: Int?, dayNumber: Int?, yerNumber: Int?) {
        routineDao.removeAllRoutine(nameMonth, dayNumber, yerNumber)
    }

    override suspend fun updateRoutine(routineModel: RoutineModel): Flow<Resource<RoutineModel?>> =
        flow {
            routineModel.apply {
                timeInMillisecond = convertDateToMilSecond(
                    this.yerNumber,
                    this.monthNumber,
                    this.dayNumber,
                    this.timeHours
                )
            }
            val equalRoutine = routineDao.checkEqualRoutine(
                routineName = routineModel.name,
                routineExplanation = routineModel.explanation ?: "",
                routineDayName = routineModel.dayName,
                routineDayNumber = routineModel.dayNumber ?: 0,
                routineYearNumber = routineModel.yerNumber ?: 0,
                routineMonthNumber = routineModel.monthNumber ?: 0,
                routineTimeMilSecond = routineModel.timeInMillisecond ?: 0,
            )
            if (equalRoutine != null) {
                emit(Resource.Error(ErrorMessageCode.EQUAL_ROUTINE_MESSAGE))
            } else {
                runCatching {
                    routineDao.updateRoutine(routineModel)
                }.onSuccess {
                    emit(Resource.Success(routineModel))
                }.onFailure {
                    emit(Resource.Error(ErrorMessageCode.EQUAL_ROUTINE_MESSAGE))
                }
            }
        }

    override suspend fun getRoutine(id: Int): RoutineModel = routineDao.getRoutine(id)
    override suspend fun checkedRoutine(routineModel: RoutineModel) {
        routineDao.updateRoutine(routineModel)
    }

    override fun getRoutines(
        monthNumber: Int, numberDay: Int, yerNumber: Int
    ): Flow<List<RoutineModel>> =
        routineDao.getRoutines(monthNumber, numberDay, yerNumber)
            .map { list -> list.map { it} }
            .distinctUntilChangedBy { it.map { it.isChecked } }

    override fun searchRoutine(
        name: String, monthNumber: Int?, dayNumber: Int?
    ): Flow<List<RoutineModel>> = routineDao.searchRoutine(name, monthNumber, dayNumber)
        .map { list -> list.map { it} }

    override fun haveAlarm(): Flow<Boolean> = routineDao.haveAlarm()
}