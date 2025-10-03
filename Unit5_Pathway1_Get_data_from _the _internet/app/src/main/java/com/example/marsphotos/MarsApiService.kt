package com.example.marsphotos
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModel
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import retrofit2.Retrofit
import retrofit2.http.GET
import java.io.IOException
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType

@Serializable
data class MarsPhoto(
    val id: String,
    @SerialName("img_src")
    val img_src: String
)

private const val BASE_URL: String = "https://android-kotlin-fun-mars-server.appspot.com"
private val retrofit =
    Retrofit.Builder().addConverterFactory(Json.asConverterFactory("application/json".toMediaType())).baseUrl(BASE_URL)
        .build()

interface MarsApiService {
    @GET("photos")
    suspend fun getPhotos(): List<MarsPhoto>
}

object SampleDataProvider {
    fun register(provider: SampleDataProvider) {
    }
}

object MarsApi {
    val retrofitService: MarsApiService by lazy { retrofit.create(MarsApiService::class.java) }
}


sealed interface MarsUiState {
    object Loading : MarsUiState
    data class Success(val photos: String) : MarsUiState
    object Error : MarsUiState
}

