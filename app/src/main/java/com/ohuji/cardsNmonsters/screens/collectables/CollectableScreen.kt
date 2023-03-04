package com.ohuji.cardsNmonsters.screens.collectables

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ohuji.cardsNmonsters.R

@Composable
fun CollectablesScreen(viewModel: CollectablesViewModel) {

    Column {
        PlayerStats(viewModel)

        ExpProgressBar(viewModel)

        AchievementList(viewModel)

    }
}

@Composable
fun PlayerStats(viewModel: CollectablesViewModel) {
    val playerStats = viewModel.getPlayerStats().observeAsState().value

    Text(text = stringResource(R.string.player_stats))
    Text(text = "${stringResource(R.string.player_level)} ${playerStats?.playerLevel}")

}

@Composable
fun AchievementList(viewModel: CollectablesViewModel) {
    val collectableList = viewModel.getAllCollectables().observeAsState(listOf())

    Text(text = stringResource(R.string.achievements))
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
            Text("${stringResource(R.string.achievement)} ${it.collectableName}")
            Text("${stringResource(R.string.progress)} ${it.currentProgress} / ${stringResource(
                R.string.requirement
            )} ${it.requirements}")
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

        Text(text = "${stringResource(R.string.exp_needed)} ${expRequired()}")

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