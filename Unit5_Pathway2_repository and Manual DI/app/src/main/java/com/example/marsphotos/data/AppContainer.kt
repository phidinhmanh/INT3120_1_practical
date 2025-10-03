package com.example.marsphotos.data

import com.example.marsphotos.network.MarsApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

private const val BASE_URL =
    "https://android-kotlin-fun-mars-server.appspot.com"



interface AppContainer {
    val marsPhotosRepository: MarsPhotoRepository
}


class DefaultAppContainer : AppContainer {
    private val retrofit: Retrofit = Retrofit.Builder().addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .build()
    private val marsApiService: MarsApiService by lazy {
        retrofit.create(MarsApiService::class.java)
    }
    override val marsPhotosRepository: MarsPhotoRepository by lazy {
        NetWorkMarsPhotoRepository(marsApiService)
    }
}