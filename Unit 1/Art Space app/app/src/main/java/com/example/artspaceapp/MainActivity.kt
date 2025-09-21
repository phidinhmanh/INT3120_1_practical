package com.example.artspaceapp

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.artspaceapp.ui.theme.ArtSpaceAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ArtSpaceAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ArtSpaceApp()
                }
            }
        }
    }
}


data class Flowers(
    val title: String,
    @DrawableRes val img: Int,
    val artist: String,
    val year: String
)

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Preview(showBackground = true, widthDp = 640, heightDp = 360, uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Landscape") // Preview chế độ ngang
@Composable
fun ArtSpaceApp() {
    var currentArtIndex by remember { mutableStateOf(0) }
    val artPieces = listOf(
        Flowers(
            "Still Life of Blue Rose and Other Flowers",
            R.drawable.flower_blue_rose,
            "Owen Scott",
            "2021"
        ),
        Flowers(
            "Vase with Tulips",
            R.drawable.flower_tulips,
            "Clara Monet",
            "1890"
        ),
        Flowers(
            "Sunflowers in a Field",
            R.drawable.flower_sunflower,
            "Vincent van Gogh",
            "1888"
        ),
        Flowers(
            "Elegant Orchid Bloom",
            R.drawable.flower_orchid,
            "Botanical Artist",
            "2023"
        )
    )
    val currentFlower = artPieces[currentArtIndex]

    fun goToNextArt() {
        currentArtIndex = (currentArtIndex + 1) % artPieces.size
    }

    fun goToPreviousArt() {
        currentArtIndex = (currentArtIndex - 1 + artPieces.size) % artPieces.size
    }

    val lightBackgroundColor = Color(0xFFF5F5F5)


    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE


    if (isLandscape) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(lightBackgroundColor)
                .padding(20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ArtImageDisplay(flower = currentFlower)
            }

            Spacer(modifier = Modifier.width(20.dp))

            Column(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                ArtTextInfo(flower = currentFlower)
                // Spacer(modifier = Modifier.height(20.dp)
                ArtNavigationButtons(onPreviousClick = { goToPreviousArt() }, onNextClick = { goToNextArt() })
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(lightBackgroundColor)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ArtImageDisplay(flower = currentFlower)
                Spacer(modifier = Modifier.height(30.dp))
                ArtTextInfo(flower = currentFlower)
            }
            ArtNavigationButtons(onPreviousClick = { goToPreviousArt() }, onNextClick = { goToNextArt() })
        }
    }
}

@Composable
fun ArtImageDisplay(flower: Flowers) {
    Box(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxWidth(0.9f)
            .aspectRatio(1f)
            .background(Color.White, RoundedCornerShape(8.dp))
            .border(width = 1.dp, color = Color.Gray.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(flower.img),
            contentDescription = flower.title,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun ArtTextInfo(flower: Flowers) {
    val subtitleColor = Color(0xFF757575)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(0xFFE6E6FA).copy(alpha = 0.5f), RoundedCornerShape(8.dp))
            .padding(10.dp)
            .widthIn(max = 400.dp)
    ) {
        Text(
            flower.title,
            style = MaterialTheme.typography.headlineMedium,
            color = Color.Black
        )
        Row {
            Text(
                flower.artist,
                fontWeight = FontWeight.Bold,
                color = subtitleColor
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                "(${flower.year})",
                color = subtitleColor
            )
        }
    }
}

@Composable
fun ArtNavigationButtons(onPreviousClick: () -> Unit, onNextClick: () -> Unit) {
    val buttonBackgroundColor = MaterialTheme.colorScheme.primary
    val buttonContentColor = Color.Black

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
    ) {
        ElevatedButton(
            onClick = onPreviousClick,
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .padding(end = 8.dp),
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = buttonBackgroundColor,
                contentColor = buttonContentColor,
                disabledContainerColor = buttonBackgroundColor.copy(alpha = 0.5f),
                disabledContentColor = buttonContentColor.copy(alpha = 0.5f)
            )
        ) {
            Text("Previous")
        }
        ElevatedButton(
            onClick = onNextClick,
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .padding(start = 8.dp),
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = buttonBackgroundColor,
                contentColor = buttonContentColor,
                disabledContainerColor = buttonBackgroundColor.copy(alpha = 0.5f),
                disabledContentColor = buttonContentColor.copy(alpha = 0.5f)
            )
        ) {
            Text("Next")
        }
    }
}