package com.rahim.data.repository.base

import kotlinx.coroutines.flow.Flow

interface BaseRepository {
    fun getCurrentTime(): List<Int>


}