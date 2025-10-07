package com.example.unit5_pathway2_bookselfapp.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.unit5_pathway2_bookselfapp.model.BookItem
import com.example.unit5_pathway2_bookselfapp.R

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onBookClick: (String) -> Unit,
    book :List<BookItem>
) {

    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2)
    ) {
        items(book.size) { index ->
            BookCard(book[index], onBookClick)
        }
    }
}


@Composable
fun BookCard(
    book: BookItem,
    onBookClick: (String) -> Unit
) {
    Card(
        onClick = { onBookClick(book.id) },
    ) {

        val rawUrl = book.volumeInfo.imageLinks?.thumbnail
        val secureUrl = rawUrl?.replace("http://", "https://")
        val imageRequest = ImageRequest.Builder(LocalContext.current)
            .data(secureUrl)
            .crossfade(true)
            .error(R.drawable.ic_launcher_foreground)
            .build()
        Box {
            AsyncImage(
                model = imageRequest,
                contentDescription = book.volumeInfo.title,
                modifier = Modifier.height(dimensionResource(R.dimen.book_card_height))
                    .width(dimensionResource(R.dimen.book_card_width))

            )
        }
    }
}


