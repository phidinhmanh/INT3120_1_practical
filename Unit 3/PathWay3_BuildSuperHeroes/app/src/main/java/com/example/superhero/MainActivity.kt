package com.example.superhero

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Label
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.superhero.ui.theme.Shapes
import com.example.superhero.ui.theme.SuperheroesTheme
import com.example.superhero.ui.theme.Typography
import com.example.superhero.ui.theme.md_theme_light_onPrimary
import com.example.superhero.ui.theme.md_theme_light_onPrimaryContainer
import com.example.superhero.ui.theme.md_theme_light_primary
import com.example.superhero.ui.theme.md_theme_light_primaryContainer

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDivideTeam = rememberSaveable { mutableStateOf(false) }
             SuperheroesTheme(darkTheme = true) {
                Scaffold(modifier = Modifier.fillMaxSize(),
                  topBar = {
                      CenterAlignedTopAppBar(
                          colors =  TopAppBarDefaults.topAppBarColors(
//                              containerColor = md_theme_light_primary,
//                              titleContentColor = md_theme_light_onPrimary
                          ),
                          title = {
                              Text(text = stringResource(R.string.app_name))
                          }
                      )
                  },
                    {
                        BottomAppBar(
                            actions = {},
                            floatingActionButton = {
                                FloatingActionButton(onClick = { isDivideTeam.value = !isDivideTeam.value }) {
                                    Text(text = "Divide Team")
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    SuperHeroList(modifier = Modifier.padding(innerPadding), isDivideTeam = isDivideTeam.value)
                }
            }
        }
    }
}

class Hero(
    val nameRes: Int,
    val descriptionRes: Int,
    val imageRes: Int
)


object HeroesRepository {
    val heroes = listOf(
        Hero(
            nameRes = R.string.hero1,
            descriptionRes = R.string.description1,
            imageRes = R.drawable.android_superhero1
        ),
        Hero(
            nameRes = R.string.hero2,
            descriptionRes = R.string.description2,
            imageRes = R.drawable.android_superhero2
        ),
        Hero(
            nameRes = R.string.hero3,
            descriptionRes = R.string.description3,
            imageRes = R.drawable.android_superhero3
        ),
        Hero(
            nameRes = R.string.hero4,
            descriptionRes = R.string.description4,
            imageRes = R.drawable.android_superhero4
        ),
        Hero(
            nameRes = R.string.hero5,
            descriptionRes = R.string.description5,
            imageRes = R.drawable.android_superhero5
        ),
        Hero(
            nameRes = R.string.hero6,
            descriptionRes = R.string.description6,
            imageRes = R.drawable.android_superhero6
        )
    )
    val goodHeroes = heroes.take(3)
    val badHeroes = heroes.drop(3)
}
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun SuperHeroList(modifier: Modifier = Modifier, isDivideTeam: Boolean = false) {
    val configuration = LocalConfiguration.current
    val islandScape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    if (islandScape && isDivideTeam) {
        Row(modifier = modifier.fillMaxWidth()) {
            LazyColumn(
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp),
                modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Text(
                        text = "Team Good",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    )
                }
                items(HeroesRepository.goodHeroes) { hero ->
                    SuperHeroCard(hero = hero)
                }
            }
            LazyColumn(
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp),
                modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Text(
                        text = "Team Bad",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    )
                }
                items(HeroesRepository.badHeroes) { hero ->
                    SuperHeroCard(hero = hero)
                }
            }
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp),
            modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (isDivideTeam) {

                item {

                    Text(
                        text = "Team Good",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    )

                }
                items(HeroesRepository.goodHeroes) { hero ->
                    SuperHeroCard(hero = hero)
                }
                item {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Team Bad",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                        )
                    }

                }
                items(HeroesRepository.badHeroes) { hero ->
                    SuperHeroCard(hero = hero)
                }

            } else {
                items(HeroesRepository.heroes) { hero ->
                    SuperHeroCard(hero = hero)
                }
            }
        }
    }
}



    @Composable
    fun SuperHeroCard(hero: Hero, modifier: Modifier = Modifier) {
        Card(
            border = BorderStroke(width = 1.dp, color = md_theme_light_primary),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = modifier.fillMaxSize()
        ) {
            Row(
                modifier = modifier.padding(16.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = modifier.padding(16.dp).weight(1f)) {
                    Text(
                        text = stringResource(hero.nameRes),
                        style = MaterialTheme.typography.displaySmall
                    )
                    Text(
                        text = stringResource(hero.descriptionRes),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Box(modifier = modifier.size(72.dp), contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(hero.imageRes),
                        contentDescription = null,
                        modifier = modifier.clip(MaterialTheme.shapes.medium)
                    )
                }
            }
        }
    }

