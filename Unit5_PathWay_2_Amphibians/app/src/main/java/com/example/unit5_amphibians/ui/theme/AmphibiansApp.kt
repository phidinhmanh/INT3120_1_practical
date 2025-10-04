package com.example.unit5_amphibians.ui.theme
import androidx.lifecycle.viewmodel.compose.viewModel
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.unit5_amphibians.ui.screen.AmphibiansViewModel
import com.example.unit5_amphibians.ui.screen.HomeScreen

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AmphibiansApp(modifier: Modifier = Modifier) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(topBar = { AmphibiansTopBar(
        scrollBehavior = scrollBehavior
    ) }, modifier = modifier) { paddingValues ->
        Surface {
            val viewModel: AmphibiansViewModel = viewModel(factory = AmphibiansViewModel.Factory)
            HomeScreen(
                amphibiansUiState = viewModel.uiState,
                retryAction = viewModel::getAmphibians,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmphibiansTopBar(scrollBehavior: TopAppBarScrollBehavior, modifier: Modifier = Modifier) {
    TopAppBar(
        scrollBehavior = scrollBehavior  ,
        title = { Text(text = "Amphibians", style = MaterialTheme.typography.headlineSmall) }
    )
}