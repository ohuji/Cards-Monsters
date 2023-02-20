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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ohuji.cardsNmonsters.R
import com.ohuji.cardsNmonsters.database.FullDeck
import com.ohuji.cardsNmonsters.screens.deck_building.DeckViewModel
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.ArNode
import io.github.sceneview.ar.node.PlacementMode
import io.github.sceneview.math.Position

@Composable
fun ARScreen(navController: NavController, viewModel: DeckViewModel) {
    val nodes = remember { mutableStateListOf<ArNode>() }
    val context = LocalContext.current

    val cardsState = viewModel.getDeckWithCards(1L).observeAsState()
    val cards: FullDeck? = cardsState.value

    var health = 1000

    val model = ArModelNode (
        placementMode = PlacementMode.BEST_AVAILABLE,
        instantAnchor = false,
        hitPosition = Position(0.0f, 0.0f, -2.0f),
        followHitPosition = true,
    ).apply {
        loadModelGlbAsync(
            context = context,
            glbFileLocation = "models/viking_animated.glb",
            autoAnimate = true,
            centerOrigin = Position(x = 0.0f, y = -1.0f, z = 0.0f),
        )
    }

    val healthBar = HealthBarNode (
        context = context,
        lifecycle = null,
    )

    if (cards != null) {
        Column() {
            Box(modifier = Modifier.fillMaxHeight(0.75f).fillMaxWidth()) {
                ARScene(
                    nodes = nodes,
                    planeRenderer = true,
                    onCreate = { arSceneView ->
                        // Apply your configuration
                        arSceneView.addChild(model)

                        arSceneView.cameraNode.addChild(healthBar)
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
            }

            Box(modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(20.dp))) {
                Image(
                    painter = painterResource(R.drawable.wood_background),
                    contentDescription = "Contact profile picture",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (i in cards.cards.indices) {
                        when (cards.cards[i].cardElement) {
                            "Fire" ->
                                Image(
                                    painter = painterResource(R.drawable.fire_card),
                                    contentDescription = "Fire card",
                                    modifier = Modifier.size(100.dp)
                                        .clickable {
                                            Log.d("TAPDBG", "tap fire")
                                            health -= 100

                                            if (health <= 0) {
                                                navController.navigate("map_screen")
                                            } else {
                                                healthBar.text = health.toString()
                                            }
                                        }
                                )
                            else ->
                                Image(
                                    painter = painterResource(R.drawable.civilian_card_back),
                                    contentDescription = "Default card",
                                    modifier = Modifier.size(100.dp)
                                        .clickable {
                                            Log.d("TAPDBG", "tap test")
                                        }
                                )
                        }
                    }
                }
            }
        }
    }
}