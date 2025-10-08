package com.example.busschedule.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [BusSchedule::class], version = 1)
abstract class BusScheduleDatabase: RoomDatabase() {
    abstract fun busScheduleDao(): BusScheduleDao

    companion object {
        @Volatile
        private var Instance : BusScheduleDatabase? = null

        fun getInstance(context: Context): BusScheduleDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, BusScheduleDatabase::class.java, "busschedule.db")
                    .fallbackToDestructiveMigration()
                    .addCallback(BusScheduleDatabaseCallback())
                    .build()
                    .also { Instance = it }
            }
        }

        private class BusScheduleDatabaseCallback() : RoomDatabase.Callback() {
            /**
             * Override the onCreate method to populate the database.
             */
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                Instance?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        val busScheduleDao = database.busScheduleDao()
                        busScheduleDao.insert(BusSchedule(1, "Main Street", 1620000000))
                        busScheduleDao.insert(BusSchedule(2, "Park Avenue", 1620003600))
                        busScheduleDao.insert(BusSchedule(3, "Oak Street", 1620007200))
                    }
                }
            }
        }
    }
}