package com.example.unit5_amphibians

import android.app.Application
import com.example.unit5_amphibians.data.AppContainer
import com.example.unit5_amphibians.data.DefaultAppContainer

class AmphibiansApplication : Application() {
    lateinit var container: AppContainer
        private set
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}
