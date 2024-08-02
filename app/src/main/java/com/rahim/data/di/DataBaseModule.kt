package com.rahim.data.di

import android.app.Application
import androidx.room.Room
import com.rahim.yadino.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataBaseModule {
    @Singleton
    @Provides
    fun providesMainDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            com.rahim.yadino.base.Constants.DATABASE_NAME
        ).build()
    }

//    @Singleton
//    @Provides
//    fun providesRoutineDao(appDatabase: com.rahim.yadino.database.AppDatabase) = appDatabase.routineDao()
//
//    @Singleton
//    @Provides
//    fun providesNoteDao(appDatabase: com.rahim.yadino.database.AppDatabase) = appDatabase.noteDao()
//    @Singleton
//    @Provides
//    fun providesTimeDao(appDatabase: com.rahim.yadino.database.AppDatabase) = appDatabase.timeDataDao()
}
