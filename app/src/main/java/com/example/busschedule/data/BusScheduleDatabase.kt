package com.example.busschedule.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [BusSchedule::class], version = 1)

abstract class BusScheduleDatabase: RoomDatabase(){
    abstract fun busScheduleDAO(): BusScheduleDAO

    companion object {
        @Volatile
        private var INSTANCE: BusScheduleDatabase? = null

        fun getDatabase(context: Context): BusScheduleDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    BusScheduleDatabase::class.java,
                    "bus_schedule_database"
                )
                    .createFromAsset("database/bus_schedule.db")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also {
                        INSTANCE = it
                    }
            }
        }
    }
}
