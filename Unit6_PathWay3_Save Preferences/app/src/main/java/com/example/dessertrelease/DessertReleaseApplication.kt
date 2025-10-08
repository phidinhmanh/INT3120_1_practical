package com.example.dessertrelease

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.example.dessertrelease.data.local.UserPreferencesRepository


private const val LAYOUT_PREFERENCE_NAME = "layout_preference"
private val Context.dataStore by preferencesDataStore(name = LAYOUT_PREFERENCE_NAME)

class DessertReleaseApplication : Application() {
    lateinit var userPreferencesRepository: UserPreferencesRepository

    override fun onCreate() {
        super.onCreate()
        userPreferencesRepository = UserPreferencesRepository(dataStore)
    }
}