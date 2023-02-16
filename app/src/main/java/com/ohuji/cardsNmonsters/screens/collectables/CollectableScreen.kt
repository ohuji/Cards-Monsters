package com.ohuji.cardsNmonsters.screens.collectables

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun CollectablesScreen(navController: NavController, viewModel: CollectablesViewModel, application: Application) {
    val collectableList = viewModel.getAllCollectables().observeAsState(listOf())
    val monsterList = viewModel.getAllMonsters().observeAsState(listOf())

    Column {
        Text(text = "Collectables")
        LazyColumn(
            contentPadding = PaddingValues(top = 8.dp, bottom = 56.dp)
        ) {
            item {
            }
            items(collectableList.value) {
                val image = it.collectableModel
                val context = LocalContext.current
                val resId = context.resources.getIdentifier(image, "drawable", context.packageName)
                Image(
                    painter = painterResource(resId),
                    contentDescription = it.collectableName
                )
                Text("Collectable: ${it.collectableName}")
                Text("Progress: ${it.currentProgress} / Requirements: ${it.requirements}")
            }
        }

        Text(text = "Monsters")

        LazyColumn(
            contentPadding = PaddingValues(top = 8.dp, bottom = 56.dp)
        ) {
            item {
            }
            items(monsterList.value) {
                Text("Monster: ${it.monsterName}")
                Text("Element: ${it.monsterElement} / HP: ${it.monsterHealth}")
            }
        }
    }
}