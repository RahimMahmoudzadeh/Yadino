package com.rahim.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.rahim.data.modle.Rotin.Routine
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutineDao {
    @Insert()
    suspend fun addRoutine(routine: Routine)

    @Query("SELECT * FROM tbl_routine WHERE id =:id")
    suspend fun getRoutine(id: Int): Routine

    @Query("SELECT * FROM tbl_routine WHERE monthName LIKE :monthName AND numberInMonth LIKE :numberDay")
    fun getRoutines(monthName: String, numberDay: Int): Flow<List<Routine>>

    @Query("DELETE FROM tbl_routine WHERE numberInMonth=:dayNumber AND monthName=:nameMonth AND yerNumber=:yerNumber")
    suspend fun removeAllRoutine(nameMonth: String?, dayNumber: String?, yerNumber: String?)

    @Delete
    suspend fun removeRoutine(routine: Routine)

    @Update
    suspend fun updateRoutine(routine: Routine)

    @Query("SELECT * FROM tbl_routine WHERE monthName=:nameMonth AND numberInMonth=:dayNumber AND name LIKE '%'||:nameRoutine|| '%'")
    fun searchRoutine(
        nameRoutine: String,
        nameMonth: String?,
        dayNumber: String?
    ): Flow<List<Routine>>
}