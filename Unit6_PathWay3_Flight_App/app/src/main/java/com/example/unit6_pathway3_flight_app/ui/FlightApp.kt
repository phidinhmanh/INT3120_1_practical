package com.example.unit6_pathway3_flight_app.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.unit6_pathway3_flight_app.ui.screen.FlightScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightAppbar() {
    TopAppBar(
        {Text("Flight Search")},
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Blue,
            titleContentColor = Color.White
        )
    )
}


@Composable
fun FlightApp() {
    Scaffold(
        topBar = { FlightAppbar() }
    ) { innerPadding ->
        FlightScreen(modifier = Modifier.padding(innerPadding))
    }
}