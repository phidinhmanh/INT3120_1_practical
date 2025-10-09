package com.example.unit6_pathway3_flight_app.ui.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.unit6_pathway3_flight_app.FlightApplication
import com.example.unit6_pathway3_flight_app.data.Airport
import com.example.unit6_pathway3_flight_app.data.AppContainer
import com.example.unit6_pathway3_flight_app.data.Favorite
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


sealed interface UiState {
    object Loading : UiState
    data class Success(val flightState: FlightUiState) : UiState
    data class Error(val message: String) : UiState
}

data class FavoriteUiState(
    val id: Int = 0,
    val departureCode: String = "",
    val destinationCode: String = "",
    val departure: String = "",
    val destination: String = "",
    val isFavorite: Boolean = false
)

data class FlightUiState(
    val departureQuery: String = "",
    val favorites: List<FavoriteUiState> = emptyList(),
    val airports: List<Airport> = emptyList()
)

fun Favorite.toUiState(isFavorite: Boolean, departure: String, destination: String): FavoriteUiState = FavoriteUiState(
    id = id,
    departure = departure,
    destination = destination,
    isFavorite = isFavorite,
    departureCode = departureCode,
    destinationCode = destinationCode
)

class FlightViewModel(appContainer: AppContainer) : ViewModel() {
    private val flightDao = appContainer.flightDatabase.flightDao()
    private val userPreferenceRepository = appContainer.userPreferenceRepository

    private val _searchQuery = MutableStateFlow("")

    private val _tempQuery = MutableStateFlow("")

    val tempQuery: StateFlow<String> = _tempQuery
    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<UiState> = _searchQuery
        .flatMapLatest { query ->
            val favoritesFlow = if (query.isBlank()) {
                userPreferenceRepository.favoriteIds.mapLatest { favoriteIds ->
                    favoriteIds.map {
                        flightDao.getFavoriteById(it.toInt()).first()!!
                    }
                }

            } else {
                flightDao.getFavoriteByDeparture(query)
            }

            favoritesFlow.combine(userPreferenceRepository.favoriteIds) { favorites, favoriteIds ->
                Pair(favorites, favoriteIds)
            }
            .mapLatest { (favorites, favoriteIds) ->
                try {
                    val favoriteUiStateList = favorites.map { favorite ->
                        favorite.toUiState(
                            isFavorite = favoriteIds.contains(favorite.id.toString()),
                            departure = flightDao.getAirportByIataCode(favorite.departureCode).first()?.name ?: "",
                            destination = flightDao.getAirportByIataCode(favorite.destinationCode).first()?.name ?: ""
                        )
                    }
                    val airports = flightDao.getAllAirports().first()
                    val flightUiState = FlightUiState(departureQuery = query, favorites = favoriteUiStateList, airports = airports)
                    UiState.Success(flightUiState) as UiState
                } catch (e: Exception) {
                    Log.e("FlightViewModel", "Error mapping favorites", e)
                    UiState.Error("Failed to load flight data.")
                }
            }
            .catch<UiState> {
                Log.e("FlightViewModel", "Error in flow", it)
                emit(UiState.Error("An error occurred in the data flow."))
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UiState.Loading
        )
    fun search() {
        _searchQuery.value = tempQuery.value
    }

    fun updateQuery(query: String) {
        _tempQuery.value = query
    }
    fun toggleFavorite(favoriteId: String, isCurrentlyFavorite: Boolean) {
        viewModelScope.launch {
            if (isCurrentlyFavorite) {
                userPreferenceRepository.removeFavorite(favoriteId)
            } else {
                userPreferenceRepository.addFavorite(favoriteId)
            }
        }
    }



    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                FlightViewModel(
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FlightApplication).container
                )
            }
        }
    }
}