package com.ohuji.cardsNmonsters.screens.deck_building

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.ohuji.cardsNmonsters.R
import com.ohuji.cardsNmonsters.database.entities.Card
import com.ohuji.cardsNmonsters.screens.augmented_reality.ShowDialog
import com.ohuji.cardsNmonsters.utils.BorderDecor
import com.ohuji.cardsNmonsters.utils.FAB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.material3.Card as materialCard

/**
 * Deck screen is used to build your deck and to view your built decks.
 *
 * @param viewModel The view model for the deck.
 * @param navController The navigation controller for the screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckScreen(viewModel: DeckViewModel, navController: NavController) {
    val deckList = viewModel.getAllDecks().observeAsState(listOf())
    val selectedCardIds = remember { mutableStateListOf<Card>() }
    val titles = listOf(stringResource(id = R.string.your_decks), stringResource(id = R.string.create_deck))
    val newestDeck = deckList.value.lastOrNull()

    var deckName by remember { mutableStateOf("") }
    var showErrorDialog by remember { mutableStateOf(false) }
    var state by remember { mutableStateOf(0) }

    Column {
        Box(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(R.drawable.cm_splash),
                contentDescription = stringResource(id = R.string.main_image_desc),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            BorderDecor()

            when (state) {
                0 -> {
                    Text(
                        text = stringResource(
                            id = R.string.view_deck),
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .padding(20.dp)
                    )
                }
                1 -> {
                    Text(
                        text = stringResource(
                            id = R.string.create_your_deck),
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .padding(20.dp)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 70.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Gray)
            ) {
                Image(
                    painter = painterResource(R.drawable.paper),
                    contentDescription = stringResource(id = R.string.paper_image_desc),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Column() {
                    TabRow(
                        selectedTabIndex = state,
                        containerColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ) {
                        titles.forEachIndexed { index, title ->
                            Tab(
                                selected = state == index,
                                onClick = { state = index },
                                text = {
                                    Text(
                                        text = title,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                },
                                icon = {
                                    when (index) {
                                        0 -> Icon(
                                            Icons.Default.List,
                                            contentDescription = stringResource(
                                                id = R.string.list_icon_desc
                                            ),
                                        )
                                        1 -> Icon(
                                            Icons.Default.Edit,
                                            contentDescription = stringResource(
                                                id = R.string.edit_icon_desc
                                            ),
                                        )
                                    }
                                }
                            )
                        }
                    }

                    when (state) {
                        0 -> {
                            DeckList(viewModel, navController)
                        }
                        1 -> {
                            Box(modifier = Modifier
                                .fillMaxHeight(0.42f)
                                .border(5.dp, MaterialTheme.colorScheme.primary)) {
                                Image(
                                    painter = painterResource(R.drawable.cm_splash),
                                    contentDescription = stringResource(id = R.string.main_image_desc),
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )

                                Column(
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .align(Alignment.Center)
                                ) {

                                    TextField(
                                        value = deckName,
                                        label = { Text(stringResource(id = R.string.deck_name)) },
                                        colors = TextFieldDefaults.textFieldColors(
                                            cursorColor = MaterialTheme.colorScheme.primary,
                                            textColor = MaterialTheme.colorScheme.primary,
                                            unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                                            unfocusedIndicatorColor = MaterialTheme.colorScheme.primary,
                                        ),
                                        modifier = Modifier
                                            .padding(top = 15.dp, bottom = 15.dp)
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
                                                    .size(64.dp)
                                                    .padding(8.dp)
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

                                    Button(
                                        modifier = Modifier
                                            .padding(top = 10.dp, bottom = 10.dp)
                                            .align(Alignment.CenterHorizontally),
                                        onClick = {
                                            if (selectedCardIds.size == 4 && deckName.length >= 3 && deckName.length < 15) {
                                                viewModel.viewModelScope.launch(Dispatchers.IO) {

                                                    viewModel.addDeck(deckName)
                                                    delay(500)
                                                }

                                                viewModel.viewModelScope.launch(Dispatchers.IO) {
                                                    viewModel.viewModelScope.launch(Dispatchers.IO) {
                                                        val newDeckId =
                                                            newestDeck?.deckId?.toInt()?.plus(1)
                                                                ?.toLong() ?: 1L

                                                        selectedCardIds.toMutableList().also {
                                                            viewModel.addCardsToDeck(newDeckId, it)
                                                            selectedCardIds.clear()
                                                        }

                                                        deckName = ""
                                                    }
                                                }
                                            } else {
                                                showErrorDialog = true

                                            }
                                        }) {
                                            Text(
                                                stringResource(id = R.string.create_deck),
                                                fontSize = 16.sp
                                            )
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
                    title = stringResource(R.string.deck_creation_error),
                    message = stringResource(R.string.deck_creation_error_message),
                    onDismiss = { showErrorDialog = false }
                )
            }
        }
    }
}

/**
 * List of cards
 *
 * @param viewModel
 * @param selectedCardIds Card objects that the user has tapped to be added to deck
 */
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
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    cardRow.forEach { card ->
                        Column {
                            Text(card.cardName)

                            val image = card.cardModel
                            val context = LocalContext.current
                            val resId = context.resources.getIdentifier(
                                image,
                                "drawable",
                                context.packageName
                            )
                            val isClickable =
                                !selectedCardIds.contains(card) && selectedCardIds.size < 4

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
            }
        )
    }
}

/**
 * List of decks the user has created. User can click a deck and then is navigated to deck details screen
 *
 * @param viewModel
 * @param navController the NavController that handles navigation
 */
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
                            contentDescription = stringResource(id = R.string.deck_image_desc),
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.7f)
                .background(Color.Black)
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(bottom = 80.dp)
            ) {
                Column() {
                    Image(
                        painter = painterResource(R.drawable.lock_icon),
                        contentDescription = stringResource(id = R.string.lock_image_desc),
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

