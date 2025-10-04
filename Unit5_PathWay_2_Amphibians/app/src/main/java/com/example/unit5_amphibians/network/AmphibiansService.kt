package com.example.unit5_amphibians.network

import com.example.unit5_amphibians.model.Amphibians
import retrofit2.http.GET


interface AmphibiansApiService {
    @GET("amphibians")
    suspend fun getAmphibians(): List<Amphibians>
}


