package com.ohuji.cardsNmonsters.screens.augmented_reality

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.ohuji.cardsNmonsters.database.FullDeck
import com.ohuji.cardsNmonsters.screens.deck_building.DeckViewModel
import dev.romainguy.kotlin.math.scale
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
            glbFileLocation = "models/adult_dragon.glb",
            autoAnimate = true,
            centerOrigin = Position(x = 0.0f, y = -1.0f, z = 0.0f),
        )
    }

    val healthBar = HealthBarNode (
        context = context,
        lifecycle = null,
    )

    if (cards != null) {
        val firstCard = CardNode(
            context = context,
            lifecycle = null,
            layout = cards.cards[0].cardElement,
            x = -0.5f,
            y = -1.1f,
        )

        val secondCard = CardNode(
            context = context,
            lifecycle = null,
            layout = cards.cards[1].cardElement,
            x = -0.17f,
            y = -1.1f,
        )

        val thirdCard = CardNode(
            context = context,
            lifecycle = null,
            layout = cards.cards[2].cardElement,
            x = 0.15f,
            y = -1.1f,
        )

        val fourthCard = CardNode(
            context = context,
            lifecycle = null,
            layout = cards.cards[3].cardElement,
            x = 0.5f,
            y = -1.1f,
        )

        Box(modifier = Modifier.fillMaxSize()) {
            ARScene(
                modifier = Modifier.fillMaxSize(),
                nodes = nodes,
                planeRenderer = true,
                onCreate = { arSceneView ->
                    // Apply your configuration
                    arSceneView.addChild(model)

                    arSceneView.cameraNode.addChild(healthBar)

                    arSceneView.cameraNode.addChild(firstCard)
                    arSceneView.cameraNode.addChild(secondCard)
                    arSceneView.cameraNode.addChild(thirdCard)
                    arSceneView.cameraNode.addChild(fourthCard)

                },
                onSessionCreate = { session ->
                    // Configure the ARCore session
                },
                onFrame = { arFrame ->
                    // Retrieve ARCore frame update
                },
                onTap = { hitResult ->
                    // User tapped in the AR view

                    if (hitResult.hitPose.ty() in -10.0..-0.5 && hitResult.hitPose.tx() in -10.0..-0.6) {
                        Log.d("TAPDBG", "firstCard TAP")
                        when(cards.cards[0].cardElement) {
                            "Fire" -> health -= 100
                        }

                        if (health <= 0) {
                            navController.navigate("map_screen")
                        } else {
                            healthBar.text = health.toString()
                        }
                    }

                    if (hitResult.hitPose.ty() in -10.0..-0.5 && hitResult.hitPose.tx() in -0.4..-0.2) {
                        Log.d("TAPDBG", "secondCard TAP")
                        when(cards.cards[0].cardElement) {
                            "Fire" -> health -= 100
                        }

                        if (health <= 0) {
                            navController.navigate("map_screen")
                        } else {
                            healthBar.text = health.toString()
                        }
                    }
                }
            )
        }
    }
}