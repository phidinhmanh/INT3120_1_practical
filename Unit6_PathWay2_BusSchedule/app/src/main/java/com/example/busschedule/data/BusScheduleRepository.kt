package com.example.busschedule.data

import kotlinx.coroutines.flow.Flow

interface BusScheduleRepository {
    fun getAllStream(): Flow<List<BusSchedule>>
    fun getForStopNameStream(stopName: String): Flow<List<BusSchedule>>
    suspend fun insert(busSchedule: BusSchedule)
    suspend fun delete(busSchedule: BusSchedule)
    suspend fun update(busSchedule: BusSchedule)
}