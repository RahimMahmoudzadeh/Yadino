package com.rahim.data.db.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.RenameTable
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import com.rahim.data.db.dao.NoteDao
import com.rahim.data.db.dao.RoutineDao
import com.rahim.data.db.dao.TimeDao
import com.rahim.data.modle.Rotin.Routine
import com.rahim.data.modle.data.TimeDate
import com.rahim.data.modle.note.NoteModel

@Database(
    entities = [Routine::class, TimeDate::class, NoteModel::class],
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