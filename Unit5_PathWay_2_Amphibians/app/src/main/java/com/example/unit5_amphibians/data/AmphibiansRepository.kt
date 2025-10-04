package com.example.unit5_amphibians.data

import com.example.unit5_amphibians.model.Amphibians
import com.example.unit5_amphibians.network.AmphibiansApiService

interface AmphibiansRepository {
    suspend fun getAmphibians(): List<Amphibians>
}

class NetWorkAmphibiansRepository(private val amphibiansApi: AmphibiansApiService
 ): AmphibiansRepository {
    override suspend fun getAmphibians(): List<Amphibians> {
        return amphibiansApi.getAmphibians()
    }
}