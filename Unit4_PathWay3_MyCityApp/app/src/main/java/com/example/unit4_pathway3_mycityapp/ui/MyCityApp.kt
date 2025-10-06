package com.example.unit4_pathway3_mycityapp.ui

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.unit4_pathway3_mycityapp.ui.screen.HomeScreen
import com.example.unit4_pathway3_mycityapp.ui.screen.PlaceAndDetailScreen
import com.example.unit4_pathway3_mycityapp.ui.screen.PlaceDetailScreen
import com.example.unit4_pathway3_mycityapp.ui.screen.PlaceScreen
import com.example.unit4_pathway3_mycityapp.ui.utils.MyCityContent


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCityAppbar(title: String, onBackClick: () -> Unit, isBackEnable: Boolean = true) {
    TopAppBar(
        title = { Text(text = title,  style = MaterialTheme.typography.titleLarge) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        navigationIcon = {
            if (isBackEnable) {
                    IconButton(
                        onClick = { onBackClick() }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }

            }
        }
    )
}

@Composable
fun MyCityApp(windowSize: WindowWidthSizeClass) {
    val navController = rememberNavController()
    val viewModel: MyCityViewModel = viewModel()


    Scaffold(
        topBar = {
            MyCityAppbar(
                title = viewModel.uiState.currentTitle,
                isBackEnable =viewModel.uiState.currentScreen != Screen.Home,
                onBackClick = {
                    viewModel.onBackClick(viewModel.uiState.currentScreen)
                    navController.popBackStack()


                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    categories = viewModel.getCategories(),
                    modifier = Modifier.padding(innerPadding),
                    onCategoryClick = { category ->
                        viewModel.onCategoryClick(category)
                        navController.navigate(Screen.Category.route)

                    }
                )
            }
            if (windowSize == WindowWidthSizeClass.Compact) {
                composable(Screen.Category.route) {
                    PlaceScreen(
                        places = viewModel.uiState.currentCategory?.places ?: return@composable,
                        modifier = Modifier.padding(innerPadding),
                        onPlaceClick = { place ->
                            viewModel.onPlaceClick(place)
                            navController.navigate(Screen.Place.route)
                        }
                    )
                }

                composable(route = Screen.Place.route) {
                    PlaceDetailScreen(
                        place = viewModel.uiState.currentPlace ?: return@composable,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            } else {
                composable(route = Screen.Category.route) {
                    val category = viewModel.uiState.currentCategory ?: return@composable

                    PlaceAndDetailScreen(
                        places = category.places,
                        place = viewModel.uiState.currentPlace ?: category.places.first(),
                        modifier = Modifier.padding(innerPadding),
                        onPlaceClick = { place ->
                            viewModel.onPlaceClick(place)
                        }
                    )
                }
            }
        }


    }

}


@Preview(showBackground = true)
@Composable
fun MyCityAppPreview() {
    MyCityApp(windowSize = WindowWidthSizeClass.Compact)
}