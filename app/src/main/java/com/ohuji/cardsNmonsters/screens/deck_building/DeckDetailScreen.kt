package com.ohuji.cardsNmonsters.screens.deck_building

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ohuji.cardsNmonsters.R
import com.ohuji.cardsNmonsters.utils.BorderDecor
import com.ohuji.cardsNmonsters.utils.FAB

@Composable
fun DeckDetailScreen(deckViewModel: DeckViewModel, deckId: Long, navController: NavController) {
    val selectedDeck = deckViewModel.getDeckWithCards(deckId).observeAsState().value?.deck
    val cardsInDeck = deckViewModel.getDeckWithCards(deckId).observeAsState().value
    var cardModelState by remember { mutableStateOf("") }

    fun deleteDeck(deckId: Long) {
        deckViewModel.deleteFullDeck(deckId)
        navController.navigate("deck_building_screen")
    }

    Column {
        Box(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(R.drawable.cm_splash),
                contentDescription = "Paper image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            BorderDecor()

            if (selectedDeck != null) {
                Row(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 10.dp)
                ) {
                    Box(modifier = Modifier.padding(top = 10.dp)) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.clickable {
                                navController.navigate("deck_building_screen")
                            }
                        )
                    }

                    Box(modifier = Modifier.padding(start = 40.dp, end = 40.dp)) {

                        Column() {
                            Text(
                                text = stringResource(id = R.string.inspect_deck),
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                            Text(
                                "- ${selectedDeck.deckName} -",
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )

                        }
                    }

                    Box(modifier = Modifier.padding(top = 10.dp)) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.clickable {
                                deleteDeck(deckId)
                            }
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.95f)
                    .padding(top = 70.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.secondary)
            ) {

                if (cardModelState.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.6f)
                            .align(Alignment.CenterHorizontally)
                            .padding(start = 75.dp, end = 75.dp, top = 20.dp, bottom = 10.dp)
                    ) {
                        val image = cardModelState
                        val context = LocalContext.current
                        val resId = context.resources.getIdentifier(
                            image,
                            "drawable",
                            context.packageName
                        )

                        Image(
                            painter = painterResource(resId),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .shadow(elevation = 20.dp),
                            contentScale = ContentScale.Crop,
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.6f)
                            .align(Alignment.CenterHorizontally)
                            .padding(start = 75.dp, end = 75.dp, top = 20.dp, bottom = 10.dp)
                            .alpha(0.55f)
                            .background(Color.Black)
                            .shadow(elevation = 20.dp)
                    )
                }


                if (selectedDeck != null && cardsInDeck != null) {
                    Log.d("DBG", "deckoo ${selectedDeck.deckId} korttii ${cardsInDeck.cards}")

                    LazyRow(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                        items(items = cardsInDeck.cards, itemContent = {
                            val image = it.cardModel
                            val context = LocalContext.current
                            val resId = context.resources.getIdentifier(
                                image,
                                "drawable",
                                context.packageName
                            )

                            Image(
                                painter = painterResource(resId),
                                contentDescription = it.cardName,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .clickable {
                                        cardModelState = it.cardModel
                                    }
                            )
                        })
                    }
                }
            }
        }
    }

    FAB(navController = navController)
}