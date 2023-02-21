package com.ohuji.cardsNmonsters.screens.deck_building

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun DeckDetailScreen(deckViewModel: DeckViewModel, deckId: Long, navController: NavController) {
    val selectedDeck = deckViewModel.getDeckWithCards(deckId).observeAsState().value?.deck
    val cardsInDeck = deckViewModel.getDeckWithCards(deckId).observeAsState().value

    fun deleteDeck(deckId: Long) {
        deckViewModel.deleteFullDeck(deckId)
        navController.navigate("deck_building_screen")
    }

Column {
    if (selectedDeck != null && cardsInDeck != null) {
    Text("Name: ${selectedDeck.deckName}")
        Log.d("DBG", "deckoo ${selectedDeck.deckId} korttii ${cardsInDeck.cards}")
        LazyColumn(
            contentPadding = PaddingValues(top = 8.dp, bottom = 56.dp)
        ) {
            item {
            }
            items(cardsInDeck.cards) { card ->
                Text(text = card.cardName)
                val image = card.cardModel
                val context = LocalContext.current
                val resId = context.resources.getIdentifier(image, "drawable", context.packageName)
                Image(
                    painter = painterResource(resId),
                    contentDescription = card.cardName,
                )
            }
        }
    } else {
        Text(text = "No deck")
    }
Row() {
    Button(onClick = { navController.navigate("deck_building_screen") }) {
        Text(text = "Back")
    }
    Button(onClick = { deleteDeck(deckId) }) {
        Text(text = "Delete deck")
    }
}
}
}