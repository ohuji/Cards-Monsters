package com.ohuji.cardsNmonsters.utils

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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
import com.ohuji.cardsNmonsters.screens.home.Home
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
    setupClusterManager: (Context, GoogleMap) -> ZoneClusterManager,
    calculateZoneViewCenter: () -> LatLngBounds,
    fusedLocationProviderClient: FusedLocationProviderClient,
) {
    val navController = rememberNavController()


    Scaffold(
        bottomBar = {
            Navbar(navController = navController)
        }
    ) {
        NavHost(navController, startDestination = "ar_screen") {
            composable("ar_screen") {
                ARScreen(
                    navController = navController,
                    viewModel = deckViewModel,
                    monsterViewModel = collectablesViewModel,
                    gameLogicViewModel = gameLogicViewModel,
                )
            }
            composable("home_screen") {

            }
            composable("map_screen") {
                MapScreen(
                    mapViewModel = mapViewModel,
                    navController = navController,
                    setupClusterManager = setupClusterManager,
                    calculateZoneViewCenter = calculateZoneViewCenter,
                    fusedLocationProviderClient = fusedLocationProviderClient,
                    deckViewModel = deckViewModel
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
            composable("collectable_screen") {

            }
            composable("guide_screen") {
                GuideScreen(navController = navController)
            }
        }
    }
}

@Composable
fun Navbar(navController: NavController) {
    val items = listOf("ar", "home", "map", "deck_building", "collectables")

    Box(modifier = Modifier.fillMaxSize()) {
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
                            "ar" -> Icon(Icons.Filled.PlayArrow, contentDescription = "AR")
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
