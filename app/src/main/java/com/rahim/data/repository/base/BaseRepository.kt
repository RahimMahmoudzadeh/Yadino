package com.rahim.data.repository.base

import kotlinx.coroutines.flow.Flow

interface BaseRepository {
    fun getCurrentTime(): List<Int>
    suspend fun getCurrentNameDay(date:String,format:String):String

}