package com.rahim.data.repository.base

import com.rahim.yadino.database.AppDatabase
import saman.zamani.persiandate.PersianDate
import javax.inject.Inject

class BaseRepositoryImpl @Inject constructor(val appDatabase: com.rahim.yadino.database.AppDatabase) : BaseRepository {
    private val persianData = PersianDate()
    private val currentTimeDay = persianData.shDay
    private val currentTimeMonth = persianData.shMonth
    private val currentTimeYer = persianData.shYear

    override fun getCurrentTime(): List<Int> = listOf(currentTimeYer,currentTimeMonth,currentTimeDay)

}