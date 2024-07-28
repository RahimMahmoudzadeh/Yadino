package com.rahim.yadino.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.RenameTable
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import com.rahim.yadino.dateTime_local.dao.TimeDao
import com.rahim.yadino.dateTime_local.dto.LocalTimeDateDto
import com.rahim.yadino.note_local.NoteDao
import com.rahim.yadino.note_local.dto.LocalNoteDto
import com.rahim.yadino.routine_local.dao.RoutineDao
import com.rahim.yadino.routine_local.dto.LocalRoutineDto

@Database(
    entities = [LocalNoteDto::class, LocalTimeDateDto::class, LocalRoutineDto::class],
    //2024/08/05
    version = 5,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(1, 2),
        AutoMigration(2, 3),
        AutoMigration(3, 4, spec = AppDatabase.Version3::class),
        AutoMigration(4, 5),
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun routineDao(): RoutineDao
    abstract fun timeDataDao(): TimeDao
    abstract fun noteDao(): NoteDao

    @DeleteColumn(
        tableName = "tbl_timeData",
        columnName = "id"
    )
    @RenameTable(fromTableName = "tbl_timeData", toTableName = "tbl_timeDate")
    class Version3: AutoMigrationSpec
}