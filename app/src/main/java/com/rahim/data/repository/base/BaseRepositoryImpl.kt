package com.rahim.data.repository.base

import com.rahim.data.db.database.AppDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import saman.zamani.persiandate.PersianDate
import saman.zamani.persiandate.PersianDateFormat
import javax.inject.Inject

class BaseRepositoryImpl @Inject constructor(val appDatabase: AppDatabase) : BaseRepository {
    private val persianData = PersianDate()
    private val currentTimeDay = persianData.shDay
    private val currentTimeMonth = persianData.shMonth
    private val currentTimeYer = persianData.shYear

    override fun getCurrentTime(): List<Int> = listOf(currentTimeYer,currentTimeMonth,currentTimeDay)

    override suspend fun getCurrentNameDay(date:String,format:String):String{
        val persianDateFormat= PersianDateFormat()
        val da=persianDateFormat.parse(date,format)
        return persianData.dayName(da)
    }
}