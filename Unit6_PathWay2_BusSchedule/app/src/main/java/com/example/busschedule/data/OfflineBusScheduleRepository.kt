package com.example.busschedule.data

import kotlinx.coroutines.flow.Flow

class OfflineBusScheduleRepository(private val dao: BusScheduleDao) : BusScheduleRepository {
    override fun getAllStream(): Flow<List<BusSchedule>> = dao.getAll()

    override fun getForStopNameStream(stopName: String): Flow<List<BusSchedule>> =
        dao.getForStopName(stopName)

    override suspend fun insert(busSchedule: BusSchedule) = dao.insert(busSchedule)

    override suspend fun delete(busSchedule: BusSchedule) = dao.delete(busSchedule)

    override suspend fun update(busSchedule: BusSchedule) = dao.update(busSchedule)
}