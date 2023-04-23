package com.rahim.data.db.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rahim.data.db.dao.RoutineDao
import com.rahim.data.db.dao.TimeDao
import com.rahim.data.modle.Rotin.Routine
import com.rahim.data.modle.data.TimeData

@Database(entities = [Routine::class, TimeData::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun routineDao(): RoutineDao
    abstract fun timeDataDao(): TimeDao
}