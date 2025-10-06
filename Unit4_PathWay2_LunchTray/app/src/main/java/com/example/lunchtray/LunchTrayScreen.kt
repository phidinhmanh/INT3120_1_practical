/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.lunchtray

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.lunchtray.datasource.DataSource
import com.example.lunchtray.ui.AccompanimentMenuScreen
import com.example.lunchtray.ui.CheckoutScreen
import com.example.lunchtray.ui.EntreeMenuScreen
import com.example.lunchtray.ui.OrderViewModel
import com.example.lunchtray.ui.SideDishMenuScreen
import com.example.lunchtray.ui.StartOrderScreen

// TODO: Screen enum

// TODO: AppBar
enum class LunchTrayScreen {
    START,
    ENTREE,
    SIDEDISH,
    ACCOMPANIMENT,
    CHECKOUT
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LunchTrayApp() {
    // TODO: Create Controller and initialization

    // Create ViewModel
    val viewModel: OrderViewModel = viewModel()
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = LunchTrayScreen.valueOf(
        backStackEntry?.destination?.route ?: LunchTrayScreen.START.name
    )

    fun cancelButtonClicked() {
        navController.popBackStack(route = LunchTrayScreen.START.name, inclusive = false)
    }


    Scaffold(
        topBar = {
            LunchTrayAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateBack = { navController.popBackStack() }
            )
        }
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()
        NavHost(
            navController = navController,
            startDestination = LunchTrayScreen.START.name ,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(LunchTrayScreen.START.name) {
                StartOrderScreen(
                    onStartOrderButtonClicked = {
                        navController.navigate(LunchTrayScreen.ENTREE.name)
                    },
                )
            }
            composable(LunchTrayScreen.ENTREE.name) {
                EntreeMenuScreen(
                    options = DataSource.entreeMenuItems,
                    onCancelButtonClicked = {
                        cancelButtonClicked()
                    },
                    onNextButtonClicked = {
                        navController.navigate(LunchTrayScreen.SIDEDISH.name)
                    },
                    onSelectionChanged = { entree ->
                        viewModel.updateEntree(entree)
                    }
                )
            }
            composable(LunchTrayScreen.SIDEDISH.name) {
                SideDishMenuScreen(
                    options = DataSource.sideDishMenuItems,
                    onCancelButtonClicked = {
                        cancelButtonClicked()
                    },
                    onNextButtonClicked = {
                        navController.navigate(LunchTrayScreen.ACCOMPANIMENT.name)
                    },
                    onSelectionChanged = { sideDish ->
                        viewModel.updateSideDish(sideDish)
                    }
                )
            }
            composable(LunchTrayScreen.ACCOMPANIMENT.name) {
                AccompanimentMenuScreen(
                    options = DataSource.accompanimentMenuItems,
                    onCancelButtonClicked = {
                        cancelButtonClicked()
                    },
                    onNextButtonClicked = {
                        navController.navigate(LunchTrayScreen.CHECKOUT.name)
                    },
                    onSelectionChanged = { accompaniment ->
                        viewModel.updateAccompaniment(accompaniment)
                    }
                )
            }
            composable(LunchTrayScreen.CHECKOUT.name) {
                CheckoutScreen(
                    orderUiState = uiState,
                    onCancelButtonClicked = {
                        navController.popBackStack(route = LunchTrayScreen.ACCOMPANIMENT.name, inclusive = false)
                    },
                    onNextButtonClicked = {
                        viewModel.resetOrder()
                        navController.popBackStack(route = LunchTrayScreen.START.name, inclusive = false)
                    }
                )
            }


        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LunchTrayAppBar(
    currentScreen: LunchTrayScreen,
    canNavigateBack: Boolean,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = {
            Text(text = stringResource(id = R.string.app_name))
        },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

