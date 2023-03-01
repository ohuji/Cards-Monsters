package com.ohuji.cardsNmonsters.screens.deck_building

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.ohuji.cardsNmonsters.database.entities.Card
import com.ohuji.cardsNmonsters.screens.augmented_reality.ShowDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckScreen(viewModel: DeckViewModel, navController: NavController) {
    val deckList = viewModel.getAllDecks().observeAsState(listOf())
    val selectedCardIds = remember { mutableStateListOf<Card>() }
    var deckName by remember { mutableStateOf("") }
    val newestDeck = deckList.value.lastOrNull()
    var showErrorDialog by remember { mutableStateOf(false) }

    Column {
        Text(text = "Create your deck", modifier = Modifier.padding(8.dp))
        TextField(
            value = deckName,
            label = { Text("Deck name") },
            onValueChange = { deckName = it })

        Row() {
            selectedCardIds.forEach { card ->
                val image = card.cardModel
                val context = LocalContext.current
                val resId = context.resources.getIdentifier(image, "drawable", context.packageName)
                Image(
                    painter = painterResource(resId),
                    contentDescription = card.cardName,
                    modifier = Modifier
                        .size(64.dp) // set the image size
                        .padding(8.dp) // add some spacing between images
                )
            }
            repeat(4 - selectedCardIds.size) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .padding(8.dp)
                        .background(Color.LightGray)
                )
            }
        }

        Button(onClick = {
            if (selectedCardIds.size == 4 && deckName.length >= 3 && deckName.length < 15) {
                viewModel.viewModelScope.launch(Dispatchers.IO) {

                    viewModel.addDeck(deckName)
                    delay(500)
                }
                viewModel.viewModelScope.launch(Dispatchers.IO) {
                    viewModel.viewModelScope.launch(Dispatchers.IO) {
                        val newDeckId = newestDeck?.deckId?.toInt()?.plus(1)?.toLong() ?: 1L
                        Log.d("DBG", "Uusi pakka $newDeckId kortit $selectedCardIds")

                        selectedCardIds.toMutableList().also {
                            viewModel.addCardsToDeck(newDeckId, it)
                            selectedCardIds.clear()
                        }
                        deckName = ""
                    }
                }
            } else {
                showErrorDialog = true
                Log.d("DBG", "Pakassa väärä määrä kortteja tai liian lyhyt nimi")
            }
        }) {
            Text("Create deck")
        }

        DeckCreationError(showErrorDialog)

        DeckList(viewModel, navController)

        CardList(viewModel, selectedCardIds)
    }
}

@Composable
fun CardList(viewModel: DeckViewModel, selectedCardIds: MutableList<Card>) {
    val cardList1 = viewModel.getAllCards().observeAsState(mutableListOf<Card>())
    val selectedCards = mutableSetOf<Card>()

    Text(text = "Cards")
    LazyColumn(
        contentPadding = PaddingValues(top = 8.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = cardList1.value.chunked(3),
            itemContent = { cardRow ->
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    cardRow.forEach { card ->
                        Column {
                            Text(card.cardName)
                            Log.d("DBG", "valitut kortit, $selectedCardIds")
                            val image = card.cardModel
                            val context = LocalContext.current
                            val resId = context.resources.getIdentifier(
                                image,
                                "drawable",
                                context.packageName
                            )
                            val isClickable = !selectedCardIds.contains(card) && selectedCardIds.size < 4

                            Image(
                                painter = painterResource(resId),
                                contentDescription = card.cardName,
                                modifier = Modifier
                                    .clickable(isClickable) {
                                        if (isClickable) {
                                            selectedCardIds += card
                                            selectedCards.add(card)
                                        }
                                    }
                                    .alpha(if (isClickable) 1f else 0.5f)
                            )
                        }
                    }
                }
            })
    }
}

@Composable
fun DeckList(viewModel: DeckViewModel, navController: NavController) {
    val deckList = viewModel.getAllDecks().observeAsState(listOf())
    Text(text = "Your decks")
    LazyColumn {
        item {
        }
        items(deckList.value) {
            Text("Deck: ${it.deckName}", Modifier.clickable {
                navController.navigate("deck_detail_screen/${it.deckId}")
            })
        }
    }
}

@Composable
fun DeckCreationError(showErrorDialog: Boolean) {
    var showDialog by remember { mutableStateOf(showErrorDialog) }
    //var showErrorDialog2 = showErrorDialog
    if (showDialog) {
        ShowDialog(
            title = "Deck creation error",
            message = "Deck must have 4 cards selected and a name with at least 3 and a maximum of 15 characters.",
            onDismiss = { showDialog = false }
        )
    }
}

