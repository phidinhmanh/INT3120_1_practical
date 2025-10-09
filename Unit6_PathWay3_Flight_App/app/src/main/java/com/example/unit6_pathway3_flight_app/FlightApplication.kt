package com.example.unit6_pathway3_flight_app

import android.app.Application
import com.example.unit6_pathway3_flight_app.data.AppContainer
import com.example.unit6_pathway3_flight_app.data.DefaultAppContainer
import com.example.unit6_pathway3_flight_app.data.FlightDataBase
import com.example.unit6_pathway3_flight_app.data.UserPreferenceRepository

class FlightApplication: Application() {

    lateinit var container : AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(
            this
        )
    }

}