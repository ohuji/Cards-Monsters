package com.ohuji.cardsNmonsters.utils

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLngBounds
import com.ohuji.cardsNmonsters.screens.augmented_reality.ARScreen
import com.ohuji.cardsNmonsters.screens.augmented_reality.GameLogicViewModel
import com.ohuji.cardsNmonsters.screens.collectables.CollectablesScreen
import com.ohuji.cardsNmonsters.screens.collectables.CollectablesViewModel
import com.ohuji.cardsNmonsters.screens.deck_building.DeckDetailScreen
import com.ohuji.cardsNmonsters.screens.deck_building.DeckScreen
import com.ohuji.cardsNmonsters.screens.deck_building.DeckViewModel
import com.ohuji.cardsNmonsters.screens.guide.GuideScreen
import com.ohuji.cardsNmonsters.screens.home.GoTViewModel
import com.ohuji.cardsNmonsters.screens.home.HomeScreen
import com.ohuji.cardsNmonsters.screens.maps.MapViewModel
import com.ohuji.cardsNmonsters.screens.maps.clusters.ZoneClusterManager
import com.ohuji.cardsNmonsters.screens.maps.compose.MapScreen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationHost(
    deckViewModel: DeckViewModel,
    collectablesViewModel: CollectablesViewModel,
    gameLogicViewModel: GameLogicViewModel,
    mapViewModel: MapViewModel,
    goTViewModel: GoTViewModel,
    setupClusterManager: (Context, GoogleMap) -> ZoneClusterManager,
    calculateZoneViewCenter: () -> LatLngBounds,
    fusedLocationProviderClient: FusedLocationProviderClient,
) {
    val navController = rememberNavController()
    val inNoBarScreen = remember { mutableStateOf(false) }

    Scaffold(
            bottomBar = {
                if (!inNoBarScreen.value) {
                Navbar(navController = navController)
            }
        }
    ) {
        NavHost(navController, startDestination = "home_screen") {
            composable("home_screen") {
                inNoBarScreen.value = true
                HomeScreen(navController = navController, gotVM = goTViewModel)
                DisposableEffect(Unit) {
                    onDispose {
                        inNoBarScreen.value = false
                    }
                }
            }

            composable("ar_screen/{monsterId}/{deckId}") {
                val monsterId = it.arguments?.getString("monsterId")?.toLong() ?: 5
                val deckId = it.arguments?.getString("deckId")?.toLong() ?: 1

                inNoBarScreen.value = true
                ARScreen(
                    navController = navController,
                    viewModel = deckViewModel,
                    monsterViewModel = collectablesViewModel,
                    gameLogicViewModel = gameLogicViewModel,
                    monsterId = monsterId,
                    deckId = deckId
                )
                DisposableEffect(Unit) {
                    onDispose {
                        inNoBarScreen.value = false
                    }
                }
            }

            composable("map_screen") {
                MapScreen(
                    mapViewModel = mapViewModel,
                    navController = navController,
                    setupClusterManager = setupClusterManager,
                    calculateZoneViewCenter = calculateZoneViewCenter,
                    fusedLocationProviderClient = fusedLocationProviderClient,
                    deckViewModel = deckViewModel,
                    monsterViewModel = collectablesViewModel,
                )
            }

            composable("deck_building_screen") {
                DeckScreen(
                    viewModel = deckViewModel,
                    navController = navController
                )
            }

            composable("deck_detail_screen/{deckId}") {
                val deckId = it.arguments?.getString("deckId")?.toLong() ?: 0

                DeckDetailScreen(deckViewModel = deckViewModel, deckId = deckId, navController = navController)
            }

            composable("collectables_screen") {
                CollectablesScreen(viewModel = collectablesViewModel)
            }

            composable("guide_screen") {
                GuideScreen(navController = navController)
            }
        }
    }
}

@Composable
fun Navbar(navController: NavController) {
    val items = listOf("home", "map", "deck_building", "collectables")

    Box(modifier = Modifier.fillMaxWidth()) {
        NavigationBar(modifier = Modifier.align(Alignment.BottomCenter)) {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    selected = navController.currentBackStackEntry?.destination?.route == "${items[index]}_screen",
                    onClick = {
                        navController.navigate("${items[index]}_screen") {
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        when (item) {
                            "home" -> Icon(Icons.Filled.Home, contentDescription = "Home")
                            "map" -> Icon(Icons.Filled.Place, contentDescription = "Map")
                            "deck_building" -> Icon(Icons.Filled.List, contentDescription = "Decks")
                            "collectables" -> Icon(
                                Icons.Filled.Star,
                                contentDescription = "Collectables"
                            )
                            else -> Icon(Icons.Default.Home, contentDescription = "Home")
                        }
                    }
                )
            }
        }
    }
}
