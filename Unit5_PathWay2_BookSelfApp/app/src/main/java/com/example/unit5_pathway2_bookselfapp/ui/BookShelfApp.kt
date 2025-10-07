package com.example.unit5_pathway2_bookselfapp.ui
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.unit5_pathway2_bookselfapp.ui.screen.HomeScreen



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookShelfAppbar() {
    CenterAlignedTopAppBar(
        title = { Text(text = "Book Shelf") },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color(0xFF8B4513),
            titleContentColor = Color.White
        ),
        modifier = Modifier.background(color = Color.Cyan)
    )
}




@Composable
fun BookShelfApp() {
    val viewModel: BookShelfViewModel = viewModel(factory = BookShelfViewModel.factory)
    val uiState by viewModel.uiState.collectAsState()
    Scaffold(
        topBar = { BookShelfAppbar() }
    )  { innerPadding ->
        when (uiState) {
            is BookUiState.Loading -> {
                // Show loading indicator
            }
            is BookUiState.Success -> {
                HomeScreen(book = (uiState as BookUiState.Success).books, onBookClick = {},
                modifier = Modifier.padding(innerPadding))
            }
            is BookUiState.Error -> {
                // Show error message
            }
        }
    }

}


@Preview(showBackground = true)
@Composable
fun BookShelfAppPreview() {
    Scaffold(
        topBar = { BookShelfAppbar() }
    ) { innerPadding ->
        HomeScreen(
            book = listOf(),
            onBookClick = {},
            modifier = Modifier.padding(innerPadding)
        )
    }
}