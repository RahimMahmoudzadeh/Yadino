package com.rahim.data.repository.base

import saman.zamani.persiandate.PersianDateFormat

interface BaseRepository {
    fun getCurrentTime(): List<Int>
    suspend fun getCurrentNameDay(date:String,format:String):String
}