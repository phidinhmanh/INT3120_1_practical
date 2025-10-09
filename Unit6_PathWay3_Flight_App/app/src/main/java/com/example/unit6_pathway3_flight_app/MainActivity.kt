package com.example.unit6_pathway3_flight_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.unit6_pathway3_flight_app.ui.FlightApp
import com.example.unit6_pathway3_flight_app.ui.theme.Unit6_PathWay3_Flight_AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Unit6_PathWay3_Flight_AppTheme {
                FlightApp()
            }
        }
    }
}
