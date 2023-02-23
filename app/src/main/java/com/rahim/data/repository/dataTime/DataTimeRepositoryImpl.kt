package com.rahim.data.repository.dataTime

import com.rahim.data.db.database.AppDatabase
import com.rahim.data.modle.data.TimeData
import com.rahim.utils.enums.WeekName
import kotlinx.coroutines.*
import saman.zamani.persiandate.PersianDate
import timber.log.Timber
import java.util.Calendar
import javax.inject.Inject
import kotlin.collections.ArrayList


class DataTimeRepositoryImpl @Inject constructor(private val appDatabase: AppDatabase) :
    DataTimeRepository {
    private val calendar = Calendar.getInstance()
    private val timeDao = appDatabase.timeDataDao()
    private val currentTime = calendar.time
    private val persianData=PersianDate()
    override suspend fun addTime() {
        if (timeDao.getAllMonthDay().isEmpty()) {
            val nameDay=persianData.dayOfWeek(currentTime)
            Timber.tag("time").d(nameDay.toString())
        }
    }

}