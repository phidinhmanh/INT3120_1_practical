package com.example.unit6_pathway3_flight_app.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserPreferenceRepository(
    private val dataStore: DataStore<Preferences>
) {

    private companion object {
        val FAVORITE_IDS = stringSetPreferencesKey("favorite_ids")
        const val TAG = "UserPreferenceRepository"
    }

    val favoriteIds: Flow<Set<String>> = dataStore.data
        .catch {
            if(it is IOException) {
                Log.e(TAG, "Error reading preferences.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[FAVORITE_IDS] ?: setOf()
        }


    suspend fun addFavorite(favoriteId: String) {
        dataStore.edit { preferences ->
            val currentFavorites = preferences[FAVORITE_IDS] ?: setOf()
            val newFavorites = currentFavorites + favoriteId
            preferences[FAVORITE_IDS] = newFavorites
        }
    }

    suspend fun removeFavorite(favoriteId: String) {
        dataStore.edit { preferences ->
            val currentFavorites = preferences[FAVORITE_IDS] ?: setOf()
            val newFavorites = currentFavorites - favoriteId
            preferences[FAVORITE_IDS] = newFavorites
        }
    }
}