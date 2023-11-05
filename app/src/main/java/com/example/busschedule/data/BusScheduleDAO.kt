package com.example.busschedule.data

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BusScheduleDAO {
    @Query("SELECT * FROM Schedule ORDER BY arrival_time")
    fun getAllItems(): Flow<List<BusSchedule>>

    @Query("SELECT * FROM Schedule WHERE stop_name = :busStopName ORDER BY arrival_time")
    fun getElemByStopName(busStopName: String): Flow<List<BusSchedule>>
}