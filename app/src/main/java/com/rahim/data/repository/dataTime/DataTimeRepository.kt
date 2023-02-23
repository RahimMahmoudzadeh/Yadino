package com.rahim.data.repository.dataTime

import com.rahim.data.modle.data.TimeData

interface DataTimeRepository {
    suspend fun addTime()
}