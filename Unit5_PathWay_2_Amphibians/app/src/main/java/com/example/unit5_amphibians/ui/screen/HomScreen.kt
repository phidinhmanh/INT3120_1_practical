package com.example.unit5_amphibians.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.unit5_amphibians.R
import com.example.unit5_amphibians.model.Amphibians


@Composable
fun HomeScreen(
    amphibiansUiState: AmphibiansUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (amphibiansUiState) {
        is AmphibiansUiState.Loading -> LoadingScreen()
        is AmphibiansUiState.Success -> ResultScreen(amphibiansUiState.amphibians,modifier = modifier)
        is AmphibiansUiState.Error -> ErrorScreen()
    }
}




@Composable
fun ResultScreen(amphibians: List<Amphibians>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.padding(dimensionResource(id = R.dimen.card_padding)),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(amphibians) { amphibians ->
            AmphibiansCard(amphibians)
        }
    }
}


@Composable
fun AmphibiansCard(amphibians: Amphibians, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(id= R.dimen.card_padding))
        ) {
            Text(amphibians.name  + "(${amphibians.type})", style = MaterialTheme.typography.headlineSmall )

            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(amphibians.imgSrc)
                    .crossfade(true)
                    .build(),
                error = painterResource(R.drawable.ic_broken_image),
                placeholder = painterResource(R.drawable.loading_img),
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().height(300.dp)
            )
            Log.d("AmphibiansCard", "Image: ${amphibians.toString()}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(amphibians.description, style = MaterialTheme.typography.bodySmall)
        }

    }
}


@Composable
fun ErrorScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text("Error", modifier = Modifier.align(Alignment.Center), style = MaterialTheme.typography.headlineLarge)
    }
}


@Composable
fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

