package com.rahim.yadino.db.di

import androidx.room.Room
import com.rahim.yadino.db.AppDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

private const val DATABASE_NAME = "main database"
val YadinoDatabaseModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
          AppDatabase::class.java,
            DATABASE_NAME
        ).build()
    }
  single { get<AppDatabase>().routineDao() }
  single { get<AppDatabase>().timeDataDao() }
  single { get<AppDatabase>().noteDao() }
}
