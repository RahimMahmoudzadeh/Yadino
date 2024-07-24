package com.rahim.data.repository.home

import com.rahim.data.db.database.AppDatabase
import com.rahim.yadino.routine.modle.Rotin.Routine
import kotlinx.coroutines.flow.Flow
import saman.zamani.persiandate.PersianDate
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(val appDatabase: AppDatabase) : HomeRepository {
    private val persianData = PersianDate()
    private val currentTimeDay = persianData.shDay
    private val currentTimeMonth = persianData.shMonth
    private val currentTimeYer = persianData.shYear

}