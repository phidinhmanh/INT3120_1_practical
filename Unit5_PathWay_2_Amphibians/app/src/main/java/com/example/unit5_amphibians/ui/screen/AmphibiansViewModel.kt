package com.example.unit5_amphibians.ui.screen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.unit5_amphibians.AmphibiansApplication
import com.example.unit5_amphibians.data.AmphibiansRepository
import kotlinx.coroutines.launch


sealed class AmphibiansUiState {
    object Loading : AmphibiansUiState()
    data class Success(val amphibians: List<com.example.unit5_amphibians.model.Amphibians>) : AmphibiansUiState()
    data class Error(val error: Throwable) : AmphibiansUiState()
}

class AmphibiansViewModel(
    private val amphibiansRepository: AmphibiansRepository,
): ViewModel() {

    var uiState: AmphibiansUiState by mutableStateOf(AmphibiansUiState.Loading)
        private set

    init {
        getAmphibians()
    }

    fun getAmphibians() {
        viewModelScope.launch {
            try {
                val amphibians = amphibiansRepository.getAmphibians()
                uiState = AmphibiansUiState.Success(amphibians)
            } catch (e: Exception) {
                uiState = AmphibiansUiState.Error(e)
                Log.d("AmphibiansViewModel", "Error: ${e.message}")
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                AmphibiansViewModel(
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AmphibiansApplication).container.amphibiansRepository
                )
            }
        }
    }

}