package com.ohuji.cardsNmonsters.screens.collectables

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun CollectablesScreen(navController: NavController, viewModel: CollectablesViewModel, application: Application) {
    val collectableList = viewModel.getAllCollectables().observeAsState(listOf())
    val monsterList = viewModel.getAllMonsters().observeAsState(listOf())
    val playerStats = viewModel.getPlayerStats().observeAsState().value

    Column {
        Text(text = "Player Stats")
        Text(text = "Current Lvl: ${playerStats?.playerLevel}")

        ExpProgressBar(viewModel)

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
                    contentDescription = it.collectableName,
                    alpha = (if (it.unlocked) 1f else 0.5f)
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

@Composable
fun ExpProgressBar(viewModel: CollectablesViewModel) {
    val playerStats = viewModel.getPlayerStats().observeAsState().value


    fun expRequired(): Int {
        val expReq = playerStats?.expRequirement ?: 1
        val currentExp = playerStats?.currentLvlExp ?: 2
        return expReq - currentExp
    }

    fun progress(): Float {
        val expReq = playerStats?.expRequirement ?: 1
        val currentExp = playerStats?.currentLvlExp ?: 2
        return currentExp.toFloat() / expReq
    }

    Column() {

        Text(text = "Exp needed for next lvl: ${expRequired()}")

        LinearProgressIndicator(
            progress = progress(),
            color = Color.Red, // set the color of the progress bar
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(8.dp)
        )
    }
}