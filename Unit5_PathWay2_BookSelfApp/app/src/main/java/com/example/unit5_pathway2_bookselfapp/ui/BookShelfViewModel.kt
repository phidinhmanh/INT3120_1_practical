package com.example.unit5_pathway2_bookselfapp.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.unit5_pathway2_bookselfapp.BookShelfApplication
import com.example.unit5_pathway2_bookselfapp.data.BookRepository
import com.example.unit5_pathway2_bookselfapp.model.BookItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface BookUiState {
    object Loading : BookUiState
    data class Success(val books: List<BookItem>) : BookUiState
    data class Error(val message: String) : BookUiState
}

class BookShelfViewModel(
    private val repository: BookRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<BookUiState>(BookUiState.Loading)
    val uiState: StateFlow<BookUiState> = _uiState.asStateFlow()

    init {
        getBooks()
    }

    fun getBooks() {
       viewModelScope.launch {
           uiState
           try {
               val books = repository.getBooks()
               _uiState.value = BookUiState.Success(books.items ?: emptyList())
               Log.d("BookShelfViewModel", "Books: ${books.items?.get(0)}")
           } catch (e: Exception) {
               Log.d("BookShelfViewModel", "Error getting books: ${e.message}")
               _uiState.value = BookUiState.Error(e.message ?: "Unknown error")
           }
       }

    }
    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                BookShelfViewModel(
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BookShelfApplication).container.bookRepository
                )
            }
        }
    }

}