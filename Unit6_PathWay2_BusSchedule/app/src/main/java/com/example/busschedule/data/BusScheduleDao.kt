package com.example.busschedule.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BusScheduleDao {
    @Query("SELECT * FROM bus_schedule ORDER BY ArrivalTimeInMillis ASC")
    fun getAll(): Flow<List<BusSchedule>>

    @Query("SELECT * FROM bus_schedule WHERE stopName = :stopName ORDER BY ArrivalTimeInMillis ASC")
    fun getForStopName(stopName: String): Flow<List<BusSchedule>>
    @Insert
    suspend fun insert(busSchedules: BusSchedule)
    @Delete
    suspend fun delete(busSchedule: BusSchedule)
    @Update
    suspend fun update(busSchedule: BusSchedule)
}