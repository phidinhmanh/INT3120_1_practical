package com.example.unit5_amphibians.data

import com.example.unit5_amphibians.network.AmphibiansApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val BASE_URL = "https://android-kotlin-fun-mars-server.appspot.com/"

interface AppContainer {
    val amphibiansRepository: AmphibiansRepository
}


class DefaultAppContainer: AppContainer {

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .build()

    private val amphibiansApi: AmphibiansApiService by lazy {
        retrofit.create(AmphibiansApiService::class.java)
    }
    override val amphibiansRepository: AmphibiansRepository by lazy {
        NetWorkAmphibiansRepository(amphibiansApi)
    }
}