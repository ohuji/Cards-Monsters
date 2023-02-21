package com.ohuji.cardsNmonsters.screens.deck_building

import android.app.Application
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    var showErrorDialog by remember { mutableStateOf(false) }

    Column {
        Text(text = "Create your deck", modifier = Modifier .padding(8.dp))
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

                val newDeckId = newestDeck?.deckId?.toInt()?.plus(1)?.toLong()
                Log.d("DBG", "Uusi pakka $newDeckId kortit $selectedCardIds")
              
                delay(500) 

                    if (newDeckId != null) {
                        viewModel.addCardsToDeck(newDeckId, selectedCardIds)
                    } else {
                        viewModel.addCardsToDeck(1L, selectedCardIds)
                    }
                    selectedCardIds = emptyList()
                    deckName = ""
                }
            } else {
               showErrorDialog = true
                Log.d("DBG", "Pakassa väärä määrä kortteja tai liian lyhyt nimi")
            }
        }) {
            Text("Create deck")
        }
        if (showErrorDialog) {
            ShowAlertDialog(
                title = "Deck creation error",
                message = "Deck must have 4 cards selected and a name with at least 3 characters and a maximum of 15.",
                onDismiss = { showErrorDialog = false }
            )
        }
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
        Text(text = "Cards")
        LazyColumn(
            contentPadding = PaddingValues(top = 8.dp, bottom = 56.dp)
        ) {
            item {
            }
            items(cardList.value) {
                    Text("Name: ${it.cardName}")
                Log.d("DBG", "valitut kortit, $selectedCardIds")
                val image = it.cardModel
                val context = LocalContext.current
                val resId = context.resources.getIdentifier(image, "drawable", context.packageName)
                Image(
                    painter = painterResource(resId),
                    contentDescription = it.cardName,
                    modifier = Modifier.clickable {  selectedCardIds += it }
                )
            }
        }
    }
}

@Composable
fun ShowAlertDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit
) {
    Log.d("DBG", "Tultiin alert")
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}

/*
    Row {

        val uber =
            newestDeck?.let { viewModel.getDeckWithCards(newestDeck.deckId).observeAsState().value }
        if (uber != null) {
            Log.d("DBG", "deckoo ${uber.deckId} korttii ${uber.cards}")
            Text(text = uber.deckName)
            LazyColumn {
                item {
                }
                items(uber.cards) { card ->
                    Text(text = card.card.cardName)
                    Log.d("DBG", "Onko kortteja ${card.card.cardName}")
                }
            }
        } else {
            Text(text = "No deck")
        }
        Spacer(modifier = Modifier.height(56.dp))
    }

 val message = if (selectedCardIds.size != 4) {
                    "Please select exactly 4 cards."
                } else {
                    "Please enter a deck name that is at least 3 characters long."
                }
                val dialog = AlertDialog(
                    onDismissRequest = { /* Dismiss the dialog */ },
                    title = { Text("Invalid deck") },
                    text = { Text(message) },
                    confirmButton = {
                        Button(onClick = { /* Dismiss the dialog */ }) {
                            Text("OK")
                        }
                    }
                )
                dialog.show()
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
 */
