package com.ohuji.cardsNmonsters.screens.collectables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ohuji.cardsNmonsters.R
import com.ohuji.cardsNmonsters.utils.BorderDecor

@Composable
fun CollectablesScreen(viewModel: CollectablesViewModel) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 50.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.cm_splash),
                contentDescription = stringResource(id = R.string.main_image_desc),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            BorderDecor()

            Column() {
                PlayerStats(viewModel)

                ExpProgressBar(viewModel)

                AchievementList(viewModel)
            }
        }
    }
}

@Composable
fun PlayerStats(viewModel: CollectablesViewModel) {
    val playerStats = viewModel.getPlayerStats().observeAsState().value

    Text(
        text = stringResource(R.string.player_stats),
        textAlign = TextAlign.Center,
        fontSize = 16.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    )

    Text(
        text = "${stringResource(R.string.player_level)} ${playerStats?.playerLevel}",
        fontSize = 16.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 20.dp)
    )
}

@Composable
fun AchievementList(viewModel: CollectablesViewModel) {
    val collectableList = viewModel.getAllCollectables().observeAsState(listOf())

    Divider(
        color = Color.White,
        thickness = 1.dp,
        modifier = Modifier.padding(top = 10.dp)
    )

    Text(
        text = stringResource(R.string.achievements),
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(),
        fontSize = 16.sp,
        textAlign = TextAlign.Center
    )

    LazyColumn(
        contentPadding = PaddingValues(top = 8.dp, bottom = 56.dp, end = 5.dp, start = 5.dp)
    ) {
        item {
        }
        items(collectableList.value) {
            Box(modifier = Modifier
                .background(color = Color.White, shape = RoundedCornerShape(8.dp))
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(8.dp)
                )
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp, top = 8.dp)

            ) {
                Row() {
                    val image = it.collectableModel
                    val context = LocalContext.current
                    val resId =
                        context.resources.getIdentifier(image, "drawable", context.packageName)

                    Image(
                        painter = painterResource(resId),
                        contentDescription = it.collectableName,
                        alpha = (if (it.unlocked) 1f else 0.5f)
                    )

                    Column() {
                        Text(it.collectableName)

                        Text(
                            "${stringResource(R.string.progress)} ${it.currentProgress} / ${it.requirements}"
                        )
                    }
                }
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
        Text(
            text = "${stringResource(R.string.exp_needed)} ${expRequired()}",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 5.dp)
        )

        LinearProgressIndicator(
            progress = progress(),
            color = Color.White, // set the color of the progress bar
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(8.dp)
        )
    }
}