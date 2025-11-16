package com.example.lab_week_10.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Total::class], version = 2, exportSchema = false)
abstract class TotalDatabase : RoomDatabase() {

    abstract fun totalDao(): TotalDao

    companion object {
        @Volatile
        private var INSTANCE: TotalDatabase? = null

        fun getDatabase(context: Context): TotalDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    TotalDatabase::class.java,
                    "total_db"
                )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
