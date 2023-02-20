package com.ohuji.cardsNmonsters.screens.deck_building

import android.app.Application
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.ohuji.cardsNmonsters.database.entities.Card
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckScreen(viewModel: DeckViewModel, navController: NavController, application: Application) {
    val deckList = viewModel.getAllDecks().observeAsState(listOf())
    val cardList = viewModel.getAllCards().observeAsState(listOf())
    var selectedCardIds by remember { mutableStateOf(emptyList<Card>()) }
    var deckName by remember { mutableStateOf("") }
    val newestDeck = deckList.value.lastOrNull()

    Column {
        TextField(
            value = deckName,
            label = { Text("Deck name") },
            onValueChange = { deckName = it })

        Button(onClick = {
            viewModel.viewModelScope.launch(Dispatchers.IO) {
                viewModel.addDeck(deckName)
                delay(500)
            }
            viewModel.viewModelScope.launch(Dispatchers.IO) {

                var newDeckId = newestDeck?.deckId?.toInt()?.plus(1)?.toLong()

                selectedCardIds = cardList.value.take(5)
                Log.d("DBG", "Uusi pakka $newDeckId kortit $selectedCardIds")
                //   viewModel.viewModelScope.launch(Dispatchers.IO) {
                delay(500) // delay for 500 ms to ensure deck is inserted
                if (newDeckId != null) {
                    viewModel.addCardsToDeck(newDeckId, selectedCardIds)
                } else {
                    viewModel.addCardsToDeck(1L, selectedCardIds)
                }
            }
            deckName = ""

        }) {
            Text("Create deck")
        }

        LazyColumn {
            item {
            }
            items(deckList.value) {
                Text("Deck: ${it.deckName}")
            }
        }
        LazyColumn(
            contentPadding = PaddingValues(top = 8.dp, bottom = 56.dp)
        ) {
            item {
            }
            items(cardList.value) {
                val image = it.cardModel
                val context = LocalContext.current
                val resId = context.resources.getIdentifier(image, "drawable", context.packageName)
                Image(
                    painter = painterResource(resId),
                    contentDescription = it.cardName
                )
                Text("Name: ${it.cardName}")
                Text("Name: ${it.cardModel}")
            }
        }

    }
    Row {

        val uber =
            newestDeck?.let { viewModel.getDeckWithCards(newestDeck.deckId).observeAsState().value }
        if (uber != null) {
            Log.d("DBG", "deckoo ${uber.deck.deckId} korttii ${uber.cards}")
            Text(text = uber.deck.deckName)
            LazyColumn {
                item {
                }
                items(uber.cards) { card ->
                    Text(text = card.cardName)
                    Log.d("DBG", "Onko kortteja ${card.cardName}")
                }
            }
        } else {
            Text(text = "No deck")
        }
        Spacer(modifier = Modifier.height(56.dp))
    }
}
