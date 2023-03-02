package com.ohuji.cardsNmonsters.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun FAB(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize().padding(bottom = 80.dp)) {
        FloatingActionButton(
            onClick = { navController.navigate("guide_screen") },
            modifier = Modifier.padding(16.dp).align(Alignment.BottomEnd)
        ) {
            Icon(Icons.Default.Info, "Localized description", tint = Color.White)
        }
    }
}
