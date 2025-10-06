package com.example.dessertclicker.ui.screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.dessertclicker.model.Dessert
import com.example.dessertclicker.data.Datasource

private val desserts = Datasource.dessertList

data class DessertUiState(
    var revenue: Int = 0,
    var dessertsSold: Int = 0,
    val currentDessertImageId: Int = 0,
    val currentDessertPrice: Int = 0
)


class DessertViewModel: ViewModel() {
    private val _dessertUiState = mutableStateOf(DessertUiState())
    val dessertUiState: State<DessertUiState> = _dessertUiState

    private fun determineDessertToShow(): Dessert {
        var dessertToShow = desserts.first()
        for (dessert in desserts) {
            if (dessertUiState.value.dessertsSold >= dessert.startProductionAmount) {
                dessertToShow = dessert
            } else {
                // The list of desserts is sorted by startProductionAmount. As you sell more desserts,
                // you'll start producing more expensive desserts as determined by startProductionAmount
                // We know to break as soon as we see a dessert who's "startProductionAmount" is greater
                // than the amount sold.
                break
            }
        }

        return dessertToShow
    }

    fun updateRevenueAndSoldDesserts(currentDessertPrice: Int) {
        _dessertUiState.value = dessertUiState.value.copy(
            revenue = dessertUiState.value.revenue + currentDessertPrice,
            dessertsSold = dessertUiState.value.dessertsSold + 1
        )
    }

    fun showNextDessert() {
        val dessertToShow = determineDessertToShow()
        _dessertUiState.value = dessertUiState.value.copy(
            currentDessertImageId = dessertToShow.imageId,
            currentDessertPrice = dessertToShow.price
        )

    }
}