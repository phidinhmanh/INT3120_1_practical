package com.example.unit4_pathway3_mycityapp.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.unit4_pathway3_mycityapp.model.Place
import com.example.unit4_pathway3_mycityapp.R

@Composable
fun PlaceDetailScreen(
    place: Place,
    modifier: Modifier = Modifier,

) {
    LazyColumn(
        verticalArrangement = Arrangement.Center,
        modifier = modifier.padding(dimensionResource(R.dimen.padding_small)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        item {
            val imageRequest = ImageRequest.Builder(LocalContext.current)
                .data(place.image)
                .crossfade(true)
                .build()
            AsyncImage(
                model = imageRequest,
                contentDescription = place.name,
                error = painterResource(R.drawable.broken),
                placeholder = painterResource(R.drawable.placeholder),
                modifier = Modifier.fillMaxWidth()
                    .height(400.dp),
                contentScale = ContentScale.Crop
            )
        }


        item {
            Text(
                text = place.description,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top=dimensionResource(R.dimen.padding_medium))
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PlaceDetailScreenPreview() {
    PlaceDetailScreen(place = Place("Café Pho Co", "https://tblg.k-img.com/restaurant/images/Rvw/62917/640x640_rect_62917116.jpg", "Café Pho Co"))
}