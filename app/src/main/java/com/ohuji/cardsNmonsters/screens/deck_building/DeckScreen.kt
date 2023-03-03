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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.ohuji.cardsNmonsters.R
import com.ohuji.cardsNmonsters.database.entities.Card
import com.ohuji.cardsNmonsters.screens.augmented_reality.ShowDialog
import com.ohuji.cardsNmonsters.utils.FAB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.material3.Card as materialCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckScreen(viewModel: DeckViewModel, navController: NavController) {
    val deckList = viewModel.getAllDecks().observeAsState(listOf())
    val selectedCardIds = remember { mutableStateListOf<Card>() }
    var deckName by remember { mutableStateOf("") }
    val newestDeck = deckList.value.lastOrNull()
    var showErrorDialog by remember { mutableStateOf(false) }
    var state by remember { mutableStateOf(0) }
    val titles = listOf(stringResource(id = R.string.your_decks), stringResource(id = R.string.create_deck))

    Column {
        when(state) {
            0 -> {
                Text(text = stringResource(id = R.string.view_deck), modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(20.dp))
            }

            1 -> {
                Text(text = stringResource(id = R.string.create_your_deck), modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(20.dp))
            }
        }

        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.Gray)) {

            Image(
                painter = painterResource(R.drawable.paper),
                contentDescription = "Paper image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Column() {
                TabRow(selectedTabIndex = state) {
                    titles.forEachIndexed { index, title ->
                        Tab(
                            selected = state == index,
                            onClick = { state = index },
                            text = { Text(text = title, maxLines = 2, overflow = TextOverflow.Ellipsis) },
                            icon = {
                                when(index) {
                                    0 -> Icon(Icons.Default.Email, contentDescription = null)
                                    1 -> Icon(Icons.Default.Home, contentDescription = null)
                                }
                            }
                        )
                    }
                }

                when(state) {
                    0 -> {
                        DeckList(viewModel, navController)
                    }

                    1 -> {
                        Box(modifier = Modifier.fillMaxHeight(0.45f)) {
                            Image(
                                painter = painterResource(R.drawable.rpg_paper_background),
                                contentDescription = "another paper image:D",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )

                            Column(modifier = Modifier
                                .padding(10.dp)
                                .align(Alignment.Center)) {

                                TextField(
                                    value = deckName,
                                    label = { Text("Deck name") },
                                    modifier = Modifier
                                        .padding(top = 20.dp, bottom = 20.dp)
                                        .background(Color.LightGray),
                                    onValueChange = { deckName = it })

                                Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                                    selectedCardIds.forEach { card ->
                                        val image = card.cardModel
                                        val context = LocalContext.current
                                        val resId =
                                            context.resources.getIdentifier(
                                                image,
                                                "drawable",
                                                context.packageName
                                            )
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

                                Button(modifier = Modifier
                                    .padding(top = 20.dp, bottom = 20.dp)
                                    .align(Alignment.CenterHorizontally), onClick = {
                                    if (selectedCardIds.size == 4 && deckName.length >= 3 && deckName.length < 15) {
                                        viewModel.viewModelScope.launch(Dispatchers.IO) {

                                            viewModel.addDeck(deckName)
                                            delay(500)
                                        }
                                        viewModel.viewModelScope.launch(Dispatchers.IO) {
                                            viewModel.viewModelScope.launch(Dispatchers.IO) {
                                                val newDeckId =
                                                    newestDeck?.deckId?.toInt()?.plus(1)?.toLong() ?: 1L
                                                Log.d(
                                                    "DBG",
                                                    "Uusi pakka $newDeckId kortit $selectedCardIds"
                                                )

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
                                    Text(stringResource(id = R.string.create_deck))
                                }


                            }
                        }
                        Box() {
                            CardList(viewModel = viewModel, selectedCardIds = selectedCardIds)
                        }
                    }
                }
            }

            FAB(navController = navController)
        }
        if (showErrorDialog) {
            ShowDialog(
                title = "Deck creation error",
                message = "Deck must have 4 cards selected and a name with at least 3 and a maximum of 15 characters.",
                onDismiss = { showErrorDialog = false }
            )
        }
    }

}

@Composable
fun CardList(viewModel: DeckViewModel, selectedCardIds: MutableList<Card>) {
    val cardList1 = viewModel.getAllCards().observeAsState(mutableListOf<Card>())
    val selectedCards = mutableSetOf<Card>()

    LazyColumn(
        contentPadding = PaddingValues(top = 10.dp, bottom = 100.dp),
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckList(viewModel: DeckViewModel, navController: NavController) {
    val deckList = viewModel.getAllDecks().observeAsState(listOf())

    if (deckList.value.isNotEmpty()) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 150.dp)
        ) {
            items(deckList.value.size) {
                materialCard(
                    onClick = { navController.navigate("deck_detail_screen/${deckList.value[it].deckId}") },
                    modifier = Modifier
                        .size(width = 180.dp, height = 150.dp)
                        .padding(15.dp)
                ) {
                    Box(Modifier.fillMaxSize()) {
                        Image(
                            painter = painterResource(R.drawable.deck1_img),
                            contentDescription = "Cards image",
                            modifier = Modifier
                                .fillMaxSize()
                                .align(Alignment.Center)
                                .background(MaterialTheme.colorScheme.background),
                            contentScale = ContentScale.Crop,
                        )

                        Text(deckList.value[it].deckName, Modifier.align(Alignment.Center))
                    }
                }
            }
        }
    } else {
        Box(modifier = Modifier
            .fillMaxSize()
            .alpha(0.7f)
            .background(Color.Black)) {
            Box(modifier = Modifier
                .align(Alignment.Center)
                .padding(bottom = 80.dp)) {
                Column() {
                    Image(
                        painter = painterResource(R.drawable.lock_icon),
                        contentDescription = "lock icon",
                        modifier = Modifier
                            .size(50.dp)
                            .align(Alignment.CenterHorizontally),
                        contentScale = ContentScale.Crop,
                    )

                    Spacer(modifier = Modifier.size(20.dp))

                    Text(text = stringResource(id = R.string.unlock_decks), color = Color.White)
                }
            }
        }
    }
}

