package com.example.unit6_pathway3_flight_app.data
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.launch
private const val FAVORITE_IDS = "favorite_ids"
private val Context.dataStore  by preferencesDataStore(name = FAVORITE_IDS)


interface AppContainer {
    val userPreferenceRepository: UserPreferenceRepository
    val flightDatabase: FlightDataBase
}

class DefaultAppContainer(private val context: Context): AppContainer {

    override val userPreferenceRepository: UserPreferenceRepository by lazy {
        UserPreferenceRepository(context.dataStore)
    }

    override val flightDatabase: FlightDataBase by lazy {
        FlightDataBase.getDatabase(context)
    }
    init {
        runBlocking {
            onCreate()
        }
    }

    private suspend fun onCreate() {
        val flightDao = flightDatabase.flightDao()
        val sampleData = listOf(
            Airport(1, "JFK", "John F. Kennedy International Airport", 10000000),
            Airport(2, "LAX", "Los Angeles International Airport", 10000000),
            Airport(3, "SFO", "San Francisco International Airport", 10000000),
            Airport(4, "ORD", "O'Hare International Airport", 10000000),
            Airport(5, "DFW", "Dallas/Fort Worth International Airport", 10000000),
            Airport(6, "ATL", "Hartsfield–Jackson Atlanta International Airport", 10000000),
        )

        val favoriteData = listOf(
            Favorite(1, "JFK", "LAX"),
            Favorite(2, "LAX", "SFO"),
            Favorite(3, "SFO", "ORD"),
            Favorite(4, "ORD", "DFW"),
            Favorite(5, "DFW", "ATL"),
            Favorite(6, "ATL", "JFK"),
        )

        coroutineScope {
            launch {
                for (airport in sampleData) {
                    flightDao.insertAirport(airport)
                }
            }

            launch {
                for (favorite in favoriteData) {
                    flightDao.insertFavorite(favorite)
                }
            }
        }
    }
}