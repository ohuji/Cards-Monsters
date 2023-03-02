package com.ohuji.cardsNmonsters.screens.augmented_reality

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ohuji.cardsNmonsters.R
import com.ohuji.cardsNmonsters.database.FullDeck
import com.ohuji.cardsNmonsters.screens.collectables.CollectablesViewModel
import com.ohuji.cardsNmonsters.screens.deck_building.DeckViewModel
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.ArNode
import io.github.sceneview.ar.node.PlacementMode
import io.github.sceneview.math.Position

@Composable
fun ARScreen(navController: NavController, viewModel: DeckViewModel, monsterViewModel: CollectablesViewModel, gameLogicViewModel: GameLogicViewModel) {
    val nodes = remember { mutableStateListOf<ArNode>() }
    val context = LocalContext.current
    val monster = monsterViewModel.findMonsterById(5L).observeAsState().value
    val cardsState = viewModel.getDeckWithCards(1L).observeAsState()
    val cards: FullDeck? = cardsState.value

    val playerStats = monsterViewModel.getPlayerStats().observeAsState().value
    var showVictoryDialog by remember { mutableStateOf(false) }
    var showDefeatDialog by remember { mutableStateOf(false) }

    var health  by remember { mutableStateOf(monster?.monsterHealth ?: 800) }


    var stateDazed by remember { mutableStateOf(false) }
    var turn by remember { mutableStateOf(0) }

    fun expRequired(): Int {
        val expReq = playerStats?.expRequirement ?: 1
        val currentExp = playerStats?.currentLvlExp ?: 2
        return expReq - currentExp
    }

    fun stateAndDamage(i: Int){
                val card = cards!!.cards[i]
        health -= gameLogicViewModel.doDamage(card.cardDamage, stateDazed, card.cardElement, monster?.monsterElement)
                val isPhysicalCard = card.cardElement == "Phys"
                stateDazed = isPhysicalCard
    }

    fun battleConclusion(): String{
        if (health <= 0) {
            gameLogicViewModel.updateCollectableTypeKill("Kill")
            gameLogicViewModel.updatePlayerStats(
                monster?.monsterHealth ?: 800
            )

            showVictoryDialog = true
        } else if (turn >= 4) {
            showDefeatDialog = true
        }
           return health.toString()
    }

    val model = ArModelNode (
        placementMode = PlacementMode.BEST_AVAILABLE,
        instantAnchor = false,
        hitPosition = Position(0.0f, 0.0f, -2.0f),
        followHitPosition = true,
    ).apply {
        loadModelGlbAsync(
            context = context,
            glbFileLocation = "models/${monster?.monsterModel}",
            autoAnimate = true,
            centerOrigin = Position(x = 0.0f, y = -1.0f, z = 0.0f),
        )
    }

    val healthBar = HealthBarNode (
        context = context,
        lifecycle = null,
    )

    if (cards != null) {
        Column {

            AR(model, nodes, turn, stateDazed, monster?.monsterName, health, monster?.monsterElement )

            Box(modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(20.dp))) {
                Image(
                    painter = painterResource(R.drawable.wood_background),
                    contentDescription = "Contact profile picture",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Column {

                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        for (i in cards.cards.indices) {
                            val image = cards.cards[i].cardModel
                            val context = LocalContext.current
                            val resId = context.resources.getIdentifier(
                                image,
                                "drawable",
                                context.packageName
                            )
                            Image(
                                painter = painterResource(resId),
                                contentDescription = cards.cards[i].cardName,
                                modifier = Modifier
                                    .size(100.dp)
                                    .clickable {
                                        Log.d("TAPDBG", "tap fire")

                                        stateAndDamage(i)

                                        turn += 1

                                        Log.d(
                                            "DBG",
                                            "Mones vuoro menos $turn paljos helttii jälel $health"
                                        )
                                        healthBar.text = battleConclusion()
                                    }
                            )
                        }

                    }
                }
            }

        }
    }
    BattleReport(showVictoryDialog, showDefeatDialog, navController, monster?.monsterName, playerStats?.playerLevel, expRequired())
}

@Composable
fun AR(model: io.github.sceneview.node.Node, nodes: List<io.github.sceneview.node.Node>, turn: Int, stateDazed: Boolean, monsterName: String?, health: Int?, monsterElement: String?) {

    Box(modifier = Modifier
        .fillMaxHeight(0.75f)
        .fillMaxWidth()) {

        ARScene(
            nodes = nodes,
            planeRenderer = true,
            onCreate = { arSceneView ->
                // Apply your configuration
                arSceneView.addChild(model)
            },
            onSessionCreate = { session ->
                // Configure the ARCore session
            },
            onFrame = { arFrame ->
                // Retrieve ARCore frame update
            },
            onTap = { hitResult ->
                // User tapped in the AR view
            }
        )
        Box(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.05f)
           /* .clip(RoundedCornerShape(20.dp))*/ ) {
            Image(
                painter = painterResource(R.drawable.wood_background),
                contentDescription = "Contact profile picture",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            TurnComposable(
                turn = turn,
                stateDazed = stateDazed,
                monsterName = monsterName, //monster?.monsterName,
                health,
                monsterElement
            )
        }
    }
}

@Composable
fun BattleReport(showVictoryDialog: Boolean, showDefeatDialog: Boolean, navController: NavController, monsterName: String?, playerLevel: Int?, expRequired: Int) {

    var showVictoryDialog = showVictoryDialog
    var showDefeatDialog = showDefeatDialog

    fun victoryDialogDismiss() {
        showVictoryDialog = false
        navController.navigate("map_screen")
    }
    fun defeatDialogDismiss() {
        showDefeatDialog= false
        navController.navigate("map_screen")
    }

    if (showVictoryDialog) {
        ShowDialog(
            title = "Monster Slain",
            message = "You have defeated $monsterName in battle. \n Current level: $playerLevel. \n Exp required to next level: $expRequired",
            onDismiss = { victoryDialogDismiss()}
        )
    }
    if (showDefeatDialog) {
        ShowDialog(
            title = "Monster fled",
            message =  "You failed to defeat $monsterName in battle",
            onDismiss = { defeatDialogDismiss()}
        )
    }
}

@Composable
fun ShowDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit
) {
    Log.d("DBG", "Tultiin alert")
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { androidx.compose.material3.Text(title) },
        text = { androidx.compose.material3.Text(message) },
        confirmButton = {
            Button(onClick = onDismiss) {
                androidx.compose.material3.Text("OK")
            }
        }
    )
}

@Composable
fun TurnComposable(turn: Int, stateDazed: Boolean, monsterName: String?, health: Int?, monsterElement: String?) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Turn $turn/4",
            textAlign = TextAlign.Left,
            fontSize = 16.sp,
            color = White,
            modifier = Modifier
                .padding(start = 8.dp, end = 0.dp, top = 0.dp, bottom = 0.dp)
                .weight(1f)
        )
        if (stateDazed) {
            Text(
                text = "$monsterName is dazed",
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                color = White,
                modifier = Modifier
                    .weight(1f)
            )
        }
            Text(
                text = "HP ${if (health != null && health < 0) { "0" } else { health }}",
                color = White,
                textAlign = TextAlign.Right,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(start = 0.dp, end = 2.dp, top = 0.dp, bottom = 0.dp)
                    .weight(1f)
            )
        val image = monsterElement?.lowercase() + "_icon"
        val context = LocalContext.current
        val resId = context.resources.getIdentifier(
            image,
            "drawable",
            context.packageName
        )
       Icon(painter = painterResource(resId), contentDescription = "Element icon",
           modifier = Modifier
               .padding(start = 0.dp, end = 5.dp, top = 0.dp, bottom = 0.dp)
               .size(20.dp))
    }
}

