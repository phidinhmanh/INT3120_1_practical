package com.example.unit4_pathway3_mycityapp.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.unit4_pathway3_mycityapp.data.DataSource
import com.example.unit4_pathway3_mycityapp.model.Category
import com.example.unit4_pathway3_mycityapp.model.MyCity
import com.example.unit4_pathway3_mycityapp.model.Place
import java.util.Stack


sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Category : Screen("category")
    object Place : Screen("place")
}

val myCity = MyCity(
    name = "Ha Noi",
    description = "Ha Noi is the capital of Vietnam.",
    categories = DataSource.categories
)


data class MyCityUiState(
    val name: String,
    val currentScreen: Screen,
    val currentTitle: String = name,
    val currentPlace: Place? = null,
    val currentCategory: Category? = null,
)

class MyCityViewModel: ViewModel() {
    var uiState by  mutableStateOf(
        MyCityUiState(
            name = myCity.name,
            currentScreen = Screen.Home
        )
    )
        private set

    fun onCategoryClick(category: Category) {
        uiState = uiState.copy(currentScreen = Screen.Category, currentTitle = category.name, currentCategory = category

        )
    }

    fun onPlaceClick(place: Place) {
        uiState = uiState.copy(currentScreen = Screen.Place, currentTitle = place.name, currentPlace = place
        )
    }

    fun getCategories() = myCity.categories



    private fun clearCategory() {
        uiState = uiState.copy(currentCategory = null)
    }

    private fun clearPlace() {
        uiState = uiState.copy(currentPlace = null)
    }

    fun onBackClick(currentScreen: Screen) {
        when (currentScreen) {
            Screen.Home -> {
                // Do nothing
            }
            Screen.Category -> {
                clearCategory()
                uiState = uiState.copy(currentScreen = Screen.Home, currentTitle = myCity.name)
            }
            Screen.Place -> {
                clearPlace()
                uiState = uiState.copy(currentScreen = Screen.Category, currentTitle = uiState.currentCategory?.name?:"")
            }
        }
    }

}