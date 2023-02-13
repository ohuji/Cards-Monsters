package com.ohuji.cardsNmonsters.utils

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ohuji.cardsNmonsters.screens.augmented_reality.ARScreen


@Composable
fun NavigationHost() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "ar_screen") {
        composable("ar_screen") {
            ARScreen(navController = navController)
        }
        composable("home_screen") {

        }
        composable("map_screen") {

        }
        composable("deck_building") {

        }
        composable("collectables_screen") {

        }
        composable("collectable_screen") {

        }
    }
}