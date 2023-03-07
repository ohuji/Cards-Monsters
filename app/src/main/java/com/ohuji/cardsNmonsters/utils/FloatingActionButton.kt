package com.ohuji.cardsNmonsters.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ohuji.cardsNmonsters.R

/**
 * Floating Action Button for screens
 * Navigates to GuideScreen when tapped
 *
 * @param navController the NavController that handles navigation
 */
@Composable
fun FAB(navController: NavController) {
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(bottom = 80.dp)) {
        FloatingActionButton(
            onClick = { navController.navigate("guide_screen") },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd),
            containerColor = MaterialTheme.colorScheme.primary,
        ) {
            Icon(
                Icons.Default.Info,
                contentDescription = stringResource(id = R.string.info_icon_desc),
                tint = Color.White
            )
        }
    }
}
