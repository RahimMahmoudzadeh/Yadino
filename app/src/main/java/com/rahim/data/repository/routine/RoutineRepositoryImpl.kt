package com.rahim.data.repository.routine

import com.rahim.data.db.dao.RoutineDao
import com.rahim.data.di.IODispatcher
import com.rahim.data.modle.Rotin.Routine
import com.rahim.data.sharedPreferences.SharedPreferencesCustom
import com.rahim.utils.resours.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import saman.zamani.persiandate.PersianDate
import saman.zamani.persiandate.PersianDateFormat
import timber.log.Timber
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

    private val equalRoutineMessage = "روتین یکسان نمی توان ساخت!!"
    override suspend fun addSampleRoutine() {
        delay(500)
        val sampleRoutines =
            routineDao.getSampleRoutines(currentTimeMonth, currentTimeDay, currentTimeYer)
        Timber.tag("sampleRoutines").d("sampleRoutines->$sampleRoutines")
        if (sharedPreferencesCustom.isShowSampleRoutine()) {
            routineDao.removeSampleRoutine()
            return
        }

        if (sampleRoutines.isNotEmpty()) return
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
                timeInMillisecond = persianData.time
            )
            routineDao.addRoutine(routine)
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
        val idAlarms = routineDao.getIdAlarmsSuspend()
        val idNotNull = ArrayList<Long>()
        for (id in idAlarms.indices) {
            if (idAlarms[id] != null) {
                idNotNull.add(idAlarms[id])
            }
        }
        return idNotNull
    }

    override suspend fun checkEdAllRoutinePastTime() {
        withContext(ioDispatcher){
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

    override suspend fun addRoutine(routine: Routine): Flow<Resource<Long>> = flow {
        emit(Resource.Loading())
        val persianDateFormat = PersianDateFormat()
        val monthDate =
            if (routine.monthNumber.toString().length == 1) "0${routine.monthNumber.toString()}" else routine.monthNumber
        val dayNumber =
            if (routine.dayNumber.toString().length == 1) "0${routine.dayNumber.toString()}" else routine.dayNumber
        val hoursDate =
            if (routine.timeHours.toString().length == 4) "0${routine.timeHours.toString()}" else routine.timeHours
        val time =
            "${routine.yerNumber}-${monthDate}-${dayNumber} ${hoursDate}:00"
        val persianDate = persianDateFormat.parse(
            time,
            "yyyy-MM-dd HH:mm:ss a"
        )
        routine.apply {
            idAlarm = getRoutineAlarmId()
            colorTask = 0
            timeInMillisecond = persianDate.time
        }
        val routines = routineDao.getRoutines()
        val equalRoutine = routines.find {
            it.name == routine.name && it.dayName == routine.dayName && it.dayNumber == routine.dayNumber && it.yerNumber == routine.yerNumber && it.monthNumber == routine.monthNumber && it.timeHours.toString()
                .replace(Regex(":"), "").toInt() == routine.timeHours.toString()
                .replace(Regex(":"), "").toInt()
        }
        if (equalRoutine == null) {
            emit(Resource.Success(routineDao.addRoutine(routine)))
        } else {
            emit(Resource.Error(equalRoutineMessage))
        }
    }.flowOn(ioDispatcher)

    private suspend fun getRoutineAlarmId(): Long {
        val idAlarms = routineDao.getIdAlarmsSuspend()
        var idRandom = Random.nextInt(0, 10000000)
        var equalIdAlarm = idAlarms.find { it == idRandom.toLong() }
        while (equalIdAlarm != null) {
            idRandom = Random.nextInt(0, 10000000)
            equalIdAlarm = idAlarms.find { it == idRandom.toLong() }
        }
        return idRandom.toLong()
    }

    override suspend fun removeRoutine(routine: Routine): Int {
        return routineDao.removeRoutine(routine)
    }

    override suspend fun removeAllRoutine(nameMonth: Int?, dayNumber: Int?, yerNumber: Int?) {
        routineDao.removeAllRoutine(nameMonth, dayNumber, yerNumber)
    }

    override suspend fun updateRoutine(routine: Routine) {
        routineDao.updateRoutine(routine)
    }

    override suspend fun getRoutine(id: Int): Routine = routineDao.getRoutine(id)


    override fun getRoutines(
        monthNumber: Int, numberDay: Int, yerNumber: Int
    ): Flow<List<Routine>> {
        Timber.tag("routineGetNameDay").d("getRoutines repo monthNumber->$monthNumber")
        Timber.tag("routineGetNameDay").d("getRoutines repo numberDay->$numberDay")
        Timber.tag("routineGetNameDay").d("getRoutines repo yerNumber->$yerNumber")
        return routineDao.getRoutines(monthNumber, numberDay, yerNumber)
    }

    override fun searchRoutine(
        name: String, monthNumber: Int?, dayNumber: Int?
    ): Flow<List<Routine>> =
        routineDao.searchRoutine(name, monthNumber, dayNumber).distinctUntilChanged()

    override suspend fun getCurrentRoutines(): Flow<List<Routine>> {
        return routineDao.getRoutines(currentTimeMonth, currentTimeDay, currentTimeYer)
    }

    override suspend fun updateCheckedByAlarmId(id: Long) {
        routineDao.updateCheckedByAlarmId(id)
    }
}