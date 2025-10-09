package com.example.unit6_pathway3_flight_app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.unit6_pathway3_flight_app.R
import com.example.unit6_pathway3_flight_app.data.Airport

@Composable
fun FlightScreen(modifier: Modifier = Modifier) {
    val viewModel: FlightViewModel = viewModel(factory = FlightViewModel.Factory)
    val uiState by viewModel.uiState.collectAsState()
    val tempQuery by viewModel.tempQuery.collectAsState()
    var showAirportList by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    when (val state = uiState) {
        is UiState.Loading -> {
            Text(text = "Loading", style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center)
        }
        is UiState.Success -> {
            val departureQuery = state.flightState.departureQuery
            LaunchedEffect(departureQuery) {
                if (tempQuery != departureQuery) {
                    viewModel.updateQuery(departureQuery)
                }
            }
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        focusManager.clearFocus()
                    }
            ) {
                SearchBar(
                    modifier = Modifier.padding(16.dp),
                    value = tempQuery,
                    onValueChange = viewModel::updateQuery,
                    onSearch = {
                        viewModel.search()
                        showAirportList = false
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    },
                    onMic = { },
                    onFocusChange = { showAirportList = it }
                )
                if (showAirportList) {
                    AirportListScreen(
                        airports = state.flightState.airports,
                        onAirportSelected = { airport ->
                            viewModel.updateQuery(airport.iataCode)
                            viewModel.search()
                            showAirportList = false
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                } else {
                    Text(
                        text = if (state.flightState.departureQuery.isBlank()) "Favorite routes" else "Flights from ${state.flightState.departureQuery}",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                    FlightList(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        favorites = state.flightState.favorites,
                        onToggleFavorite = { id, isFavorite -> viewModel.toggleFavorite(id, isFavorite) }
                    )
                }
            }
        }

        is UiState.Error -> {
            Text(text = "Error: ${state.message}", style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center)
        }
    }
}


@Composable
fun FlightList(
    modifier: Modifier = Modifier,
    favorites: List<FavoriteUiState>,
    onToggleFavorite: (String, Boolean) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(favorites) { favorite ->
            FlightCard(
                modifier = Modifier.padding(vertical = 8.dp),
                favoriteUiState = favorite,
                onToggleFavorite = onToggleFavorite
            )
        }
    }
}

@Composable
fun FlightCard(
    modifier: Modifier = Modifier,
    favoriteUiState: FavoriteUiState,
    onToggleFavorite: (String, Boolean) -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(100.dp),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(text = "DEPART", style = MaterialTheme.typography.bodySmall)
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = favoriteUiState.departureCode, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = favoriteUiState.departure,
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        softWrap = false
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = favoriteUiState.destinationCode, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = favoriteUiState.destination,
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        softWrap = false
                    )
                }
                Text(text = "ARRIVE", style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = { onToggleFavorite(favoriteUiState.id.toString(), favoriteUiState.isFavorite) }) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Favorite",
                    tint = if (favoriteUiState.isFavorite) MaterialTheme.colorScheme.primary else Color.LightGray
                )
            }
        }
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    onSearch: () -> Unit,
    onMic: () -> Unit,
    onFocusChange: (Boolean) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { /* consume click */ }
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = { onSearch() }
            ) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
            }
            TextField(
                value = value,
                onValueChange = onValueChange,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { onSearch() }),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                    errorContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                label = { Text("Search") },
                singleLine = true,
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .onFocusChanged { focusState ->
                        onFocusChange(focusState.isFocused)
                        if (focusState.isFocused) {
                            keyboardController?.show()
                        } else {
                            keyboardController?.hide()
                            focusManager.clearFocus()

                        }
                    }
            )
        }

        IconButton(
            onClick = {
                onMic()
                focusRequester.requestFocus()
            }
        ) {
            Icon(
                painter = painterResource(R.drawable.microphone),
                contentDescription = "Mic",
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}


@Composable
fun AirportListScreen(
    airports: List<Airport>,
    onAirportSelected: (Airport) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(airports) { airport ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onAirportSelected(airport) }
                    .padding(vertical = 12.dp, horizontal = 4.dp)
            ) {
                Text(text = airport.iataCode, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = airport.name, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SearchBarPreview() {
    var text by remember { mutableStateOf("") }
    SearchBar(
        modifier = Modifier.padding(16.dp),
        value = text,
        onValueChange = { text = it },
        onSearch = {},
        onMic = {},
        onFocusChange = {}
    )
}

@Preview
@Composable
fun FlightCardPreview() {
    FlightCard(
        modifier = Modifier,
        favoriteUiState = FavoriteUiState(
            id = 1,
            departureCode = "JFK",
            destinationCode = "LAX",
            departure = "New York",
            destination = "Los Angeles International Airport",
            isFavorite = true
        ),
        onToggleFavorite = { _, _ -> }
    )
}
