package com.ohuji.cardsNmonsters.screens.guide

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun GuideScreen(navController: NavController) {

    Column {
        Guide()
        GuideDeckBuilding()
        GuideMap()
        GuideBattle()
        GuideElement()
        BackButton(navController = navController)
    }
}

@Composable
fun Guide() {
    Text(text = "How to play Cards & Monsters")
}

@Composable
fun GuideDeckBuilding() {
    Text(text = "How to build your deck")
}

@Composable
fun GuideMap() {
    Text(text = "Map")
}

@Composable
fun GuideBattle() {
    Text(text = "Battle")
}

@Composable
fun GuideElement() {
    Text(text = "Elemental strength's and weaknesses")
}

@Composable
fun BackButton(navController: NavController) {
    Button(onClick = { navController.navigate("home_screen") }) {
        Text(text = "Back")
    }
}