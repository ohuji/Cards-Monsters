package com.ohuji.cardsNmonsters.utils

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ohuji.cardsNmonsters.screens.augmented_reality.ARScreen
import com.ohuji.cardsNmonsters.screens.deck_building.DeckScreen
import com.ohuji.cardsNmonsters.screens.deck_building.DeckViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationHost(deckViewModel: DeckViewModel) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar(navController = navController)
        }
    ) {
        NavHost(navController, startDestination = "deck_building_screen") {
            composable("ar_screen") {
                ARScreen(navController = navController)
            }
            composable("home_screen") {

            }
            composable("map_screen") {

            }
            composable("deck_building_screen") {
                DeckScreen(viewModel = deckViewModel, navController = navController, application = Application())
            }
            composable("collectables_screen") {

            }
            composable("collectable_screen") {

            }
        }
    }
}

@Composable
fun NavigationBar(navController: NavController) {
    val items = listOf("ar", "home", "map", "deck_building", "collectables")

    Box(modifier = Modifier.fillMaxSize()) {
        BottomNavigation(modifier = Modifier.align(Alignment.BottomCenter)) {
            items.forEachIndexed { index, item ->
                BottomNavigationItem(
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
                            "collectables" -> Icon(Icons.Filled.Star, contentDescription = "Collectables")
                            else -> Icon(Icons.Default.Home, contentDescription = "Home")
                        }
                    }
                )
            }
        }
    }
}
