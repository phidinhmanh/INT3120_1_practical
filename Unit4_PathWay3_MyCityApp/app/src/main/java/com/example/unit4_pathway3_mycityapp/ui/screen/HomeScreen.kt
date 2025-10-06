package com.example.unit4_pathway3_mycityapp.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.unit4_pathway3_mycityapp.R
import com.example.unit4_pathway3_mycityapp.data.DataSource
import com.example.unit4_pathway3_mycityapp.model.Category
import com.example.unit4_pathway3_mycityapp.model.Place


@Composable
fun HomeScreen(categories: List<Category>, modifier: Modifier = Modifier, onCategoryClick: (Category) -> Unit) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(categories) {index, category ->
            CategoryRow(category = category, onCategoryClick = onCategoryClick)
            if (index < categories.size - 1) {
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp,
                    color = androidx.compose.material3.DividerDefaults.color
                )
            }
        }
    }
}


@Composable
fun CategoryRow(category: Category, onCategoryClick: (Category) -> Unit) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.clickable { onCategoryClick(category) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.placeholder),
                contentDescription = null,
                modifier = Modifier.size(50.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))
            Text(text = category.name, modifier = Modifier.weight(1f) )

        }
    }
}


@Composable
fun PlaceScreen(places :List<Place>, modifier: Modifier = Modifier, onPlaceClick: (Place) -> Unit) {

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        itemsIndexed(places) {index, place ->
            PlaceRow(place = place, onPlaceClick = onPlaceClick)
            if (index < places.size - 1) {
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp,
                    color = androidx.compose.material3.DividerDefaults.color
                )
            }
        }
    }
}


@Composable
fun PlaceRow(place: Place, onPlaceClick: (Place) -> Unit) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.clickable { onPlaceClick(place) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.placeholder),
                contentDescription = null,
                modifier = Modifier.size(50.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))
            Text(text = place.name, modifier = Modifier.weight(1f) )

        }
    }
}



@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(categories = DataSource.categories, onCategoryClick = {})
}


@Composable
fun PlaceAndDetailScreen(
    modifier: Modifier = Modifier,
    onPlaceClick: (Place) -> Unit,
    places: List<Place>,
    place: Place

) {

    Box {
        Row {
            PlaceScreen(places = places, onPlaceClick = onPlaceClick, modifier = modifier.weight(1f))
            PlaceDetailScreen(place = place, modifier = modifier.weight(1f))
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PlaceAndDetailScreenPreview() {
    PlaceAndDetailScreen(places = DataSource.categories.first().places, place = DataSource.categories.first().places.first(), onPlaceClick = {})
}


